package com.auth.framework.core.encryption;


import com.auth.framework.core.exceptions.EncryptionException;

/**
 * Сервис для аутентифицировающего шифрования/дешифрования данных, которые могут быть представлены в виде строки
 */
public interface EncryptionService {

    /**
     * @param input входные данные
     * @return зашифрованная строка, в случае успеха
     * @throws RuntimeException если не удалось зашифровать данные
     */
    String encrypt(String input) throws EncryptionException;

    /**
     * @param input входные данные
     * @return расшифрованная строка, в случае успеха
     * @throws RuntimeException если не удалось расшифровать данные
     */
    String decrypt(String input) throws EncryptionException;
}
