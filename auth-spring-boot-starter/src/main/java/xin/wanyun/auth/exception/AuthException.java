package xin.wanyun.auth.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AuthException extends Exception{
    private String message;
}
