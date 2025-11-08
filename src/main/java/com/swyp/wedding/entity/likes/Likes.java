package com.swyp.wedding.entity.likes;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
}
