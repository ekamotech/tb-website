package com.example.website.validation.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

/**
 * 画像のByteサイズを検証するバリデータークラス。
 */
public class ImageByteValidator implements ConstraintValidator<ImageByte, MultipartFile> {

    int max;

    /**
     * アノテーションの初期化メソッド。
     *
     * @param annotation ImageByte アノテーション
     */
    @Override
    public void initialize(ImageByte annotation) {
        this.max = annotation.max();
    }

    /**
     * 画像のByteサイズが指定された最大値を超えていないかを検証します。
     *
     * @param image 検証対象の画像ファイル
     * @param context コンテキスト
     * @return 画像のByteサイズが最大値を超えない場合は true、それ以外の場合は false
     */
    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {
        if (image.getSize() > this.max) {
            return false;
        }
        return true;
    }
}

