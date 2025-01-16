package com.example.website.validation.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

/**
 * 画像フィールドが空でないことを検証するバリデータークラス。
 */
public class ImageNotEmptyValidator implements ConstraintValidator<ImageNotEmpty, MultipartFile> {

    /**
     * アノテーションの初期化メソッド。
     *
     * @param annotation ImageNotEmpty アノテーション
     */
    @Override
    public void initialize(ImageNotEmpty annotation) {
    }

    /**
     * 画像フィールドが空でないことを検証します。
     *
     * @param image 検証対象の画像ファイル
     * @param context コンテキスト
     * @return 画像フィールドが空でない場合は true、それ以外の場合は false
     */
    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {
        if (image.isEmpty()) {
            return false;
        }
        return true;
    }
}

