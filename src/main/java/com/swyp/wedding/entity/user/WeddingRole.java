package com.swyp.wedding.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeddingRole {
    GROOM("신랑"),
    BRIDE("신부");

    private final String label;

    /**
     * weddingRole : GROOM
     * weddingRole : 신랑
     * 아래 코드는 둘 중 어느 값으로 들어와도 상관 없음.
     */
    @JsonCreator
    public static WeddingRole from(String input) {
        for (WeddingRole role : values()) {
            if (role.name().equalsIgnoreCase(input) || role.label.equals(input)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid weddingRole: " + input);
    }
}
