package com.swyp.wedding.dto.auth;

import lombok.Getter;
import com.swyp.wedding.entity.user.*;
import java.time.LocalDate;

@Getter
public class OAuthExtraInfoRequest {

    private String nickname;
    private LocalDate birth;
    private LocalDate weddingDate;
    private WeddingRole weddingRole;

}
