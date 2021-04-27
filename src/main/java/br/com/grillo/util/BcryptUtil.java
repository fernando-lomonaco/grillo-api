package br.com.grillo.util;

import br.com.grillo.exception.PasswordException;
import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptUtil {

    private BcryptUtil() {
        throw new AssertionError("Classe nao pode ser instanciada");
    }

    public static String getHash(@NonNull String password) {

        if (BcryptUtil.isEncrypted(password)) {
            return password;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static boolean isEncrypted(String password) {
        return password.startsWith("$2a$");
    }

    public static String decode(String password, String encrypted) throws PasswordException {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        boolean isPasswordMatches = bcrypt.matches(password, encrypted);

        if (!isPasswordMatches)
            throw new PasswordException("Password does not match.");

        return password;
    }
}
