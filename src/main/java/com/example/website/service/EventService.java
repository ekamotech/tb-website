package com.example.website.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import com.example.website.entity.Comment;
import com.example.website.entity.Event;
import com.example.website.entity.EventAttendee;
import com.example.website.entity.Favorite;
import com.example.website.entity.Group;
import com.example.website.entity.User;
import com.example.website.entity.UserInf;
import com.example.website.form.CommentForm;
import com.example.website.form.EventForm;
import com.example.website.form.FavoriteForm;
import com.example.website.form.GroupForm;
import com.example.website.form.UserForm;
import com.example.website.repository.EventAttendeeRepository;
import com.example.website.repository.EventRepository;
import com.example.website.repository.GroupRepository;
import com.example.website.repository.UserRepository;

@Service
public class EventService {
    
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HttpServletRequest request;
    
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final EventRepository eventRepository;
    private final EventAttendeeRepository eventAttendeeRepository;
    private final EventAttendeeService eventAttendeeService;
    private final SendMailService sendMailService;

    public EventService(UserRepository userRepository, GroupRepository groupRepository, EventRepository eventRepository, EventAttendeeRepository eventAttendeeRepository, EventAttendeeService eventAttendeeService, SendMailService sendMailService) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.eventRepository = eventRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.eventAttendeeService = eventAttendeeService;
        this.sendMailService = sendMailService;
    }
    
    @Value("${image.local:false}")
    private String imageLocal;
    
    public Event findById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return event;
    }
    
    public List<EventForm> index(Principal principal) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf userInf = (UserInf) authentication.getPrincipal();
        
        List<Event> events = eventRepository.findAllByOrderByUpdatedAtDesc();
        List<EventForm> list = new ArrayList<>();
        for (Event entity : events) {
            EventForm form = getEvent(userInf, entity);
            list.add(form);
        }
        return list;
    }
    
    public EventForm getEvent(UserInf user, Event entity) throws FileNotFoundException, IOException {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(Event.class, EventForm.class).addMappings(mapper -> mapper.skip(EventForm::setUser));
        modelMapper.typeMap(Event.class, EventForm.class).addMappings(mapper -> mapper.skip(EventForm::setGroup));
        
        modelMapper.typeMap(Event.class, EventForm.class).addMappings(mapper -> mapper.skip(EventForm::setFavorites));
        modelMapper.typeMap(Favorite.class, FavoriteForm.class).addMappings(mapper -> mapper.skip(FavoriteForm::setEvent));
        
        modelMapper.typeMap(Event.class, EventForm.class).addMappings(mapper -> mapper.skip(EventForm::setComments));
        
        EventForm form = modelMapper.map(entity, EventForm.class);
        
        boolean isImageLocal = false;
        if (imageLocal != null) {
            isImageLocal = new Boolean(imageLocal);
        }
        if (isImageLocal) {
            try (InputStream is = new FileInputStream(new File(entity.getPath()));
                    ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                byte[] indata = new byte[10240 * 16];
                int size;
                while ((size = is.read(indata, 0, indata.length)) > 0) {
                    os.write(indata, 0, size);
                }
                StringBuilder data = new StringBuilder();
                data.append("data:");
                data.append(getMimeType(entity.getPath()));
                data.append(";base64,");

                data.append(new String(Base64Utils.encode(os.toByteArray()), "ASCII"));
                form.setImageData(data.toString());
            }
        }
        
        UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
        form.setUser(userForm);
        
        GroupForm groupForm = modelMapper.map(entity.getGroup(), GroupForm.class);
        form.setGroup(groupForm);
        
        List<FavoriteForm> favorites = new ArrayList<FavoriteForm>();
        for (Favorite favoriteEntity : entity.getFavorites()) {
            FavoriteForm favorite = modelMapper.map(favoriteEntity, FavoriteForm.class);
            favorites.add(favorite);
            if (user.getUserId().equals(favoriteEntity.getUser().getUserId())) {
                form.setFavorite(favorite);
            }
        }
        form.setFavorites(favorites);
        
        List<CommentForm> comments = new ArrayList<CommentForm>();
        for (Comment commentEntity : entity.getComments()) {
            CommentForm comment = modelMapper.map(commentEntity, CommentForm.class);
            comments.add(comment);
        }
        form.setComments(comments);
        
        return form;
    }
    
    private String getMimeType(String path) {
        String extension = FilenameUtils.getExtension(path);
        String mimeType = "image/";
        switch (extension) {
        case "jpg":
        case "jpeg":
            mimeType += "jpeg";
            break;
        case "png":
            mimeType += "png";
            break;
        case "gif":
            mimeType += "gif";
            break;
        }
        return mimeType;
    }
    
    public EventForm createEventForm(Long userId, Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("指定されたグループは見つかりませんでした"));

        EventForm form = new EventForm();
        form.setUserId(userId);
        form.setGroupId(group.getId());
        return form;
    }
    
    @Transactional
    public void createEvent(Long userId, Long groupId, EventForm form, MultipartFile image) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("指定されたグループは見つかりませんでした"));
        
        Event event = new Event();
        event.setUser(user);
        event.setGroup(group);
        event.setTitle(form.getTitle());
        event.setDescription(form.getDescription());
        event.setDate(form.getDate());
        event.setStartTime(form.getStartTime());
        event.setEndTime(form.getEndTime());
        
        boolean isImageLocal = false;
        if (imageLocal != null) {
            isImageLocal = new Boolean(imageLocal);
        }
        
        File destFile = null;
        if (isImageLocal) {
            destFile = saveImageLocal(image, event);
            event.setPath(destFile.getAbsolutePath());
        } else {
            event.setPath("");
        }
        
        event.setAddress(form.getAddress());
        event.setLatitude(form.getLatitude());
        event.setLongitude(form.getLongitude());

        eventRepository.saveAndFlush(event);
        
        EventAttendee eventAttendee = new EventAttendee();
        eventAttendee.setEvent(event);
        eventAttendee.setUser(user);
        eventAttendee.setParticipationStatus(EventAttendee.ParticipationStatus.PARTICIPATING);
        eventAttendeeRepository.saveAndFlush(eventAttendee);
        
    }
    
    private File saveImageLocal(MultipartFile image, Event event) throws IOException {
        File uploadDir = new File("/uploads");
        uploadDir.mkdir();
        
        String uploadsDir = "/uploads/";
        String realPathToUploads = request.getServletContext().getRealPath(uploadsDir);
        if (!new File(realPathToUploads).exists()) {
            new File(realPathToUploads).mkdir();
        }
        
        String originalFilename = image.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        
        File destFile = new File(realPathToUploads, uniqueFileName);
        image.transferTo(destFile);

        return destFile;
    }
    
    @Transactional
    public void joinEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("イベントが見つかりません"));

        // 参加登録を委譲
        eventAttendeeService.saveAttendee(user, event);
        
        // 参加登録メールを送信
        Context context = new Context();
        context.setVariable("title", "イベントに参加登録しました！");
        context.setVariable("name", user.getName());
        context.setVariable("event", event.getTitle());
        sendMailService.sendMail(context, user.getUsername(), "email");

    }
    
    
}
