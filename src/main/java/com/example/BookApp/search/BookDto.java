package com.example.BookApp.search;

import lombok.*;


@Getter
@ToString
@NoArgsConstructor
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String imageURL;

    @Builder
    public BookDto(String title,String author,String imageURL){
        this.title=title;
        this.author=author;
        this.imageURL=imageURL;
    }

}
