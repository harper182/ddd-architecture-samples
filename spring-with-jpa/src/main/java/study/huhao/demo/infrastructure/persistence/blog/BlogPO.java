package study.huhao.demo.infrastructure.persistence.blog;

import lombok.*;
import study.huhao.demo.domain.models.blog.Blog;
import study.huhao.demo.domain.models.blog.BlogId;
import study.huhao.demo.domain.models.user.UserId;
import study.huhao.demo.infrastructure.persistence.PersistenceObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

// Spring Data JPA annotations
@Entity
@Table(name = "blog")
// Lombok annotations
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class BlogPO implements PersistenceObject<Blog> {

    @Id
    private String id;
    private String title;
    private String body;
    private String authorId;
    private String status;
    private Instant createdAt;
    private Instant savedAt;
    private PublishedBlogPO published;

    // The persistence object needs to reflect the table structure.
    // The domain model and persistence object may have much different.
    // So, manual to convert between them is better than use object mapper like Orika.
    @Override
    public Blog toDomainModel() {
        return new Blog(
                BlogId.valueOf(id),
                title,
                body,
                UserId.valueOf(authorId),
                Blog.Status.valueOf(status),
                createdAt,
                savedAt,
                published == null ? null : published.toDomainModel()
        );
    }

    // The persistence object needs to reflect the table structure.
    // The domain model and persistence object may have much different.
    // So, manual to convert between them is better than use object mapper like Orika.
    static BlogPO of(Blog blog) {
        if (blog == null) return null;

        return BlogPO.builder()
                .id(blog.getId().toString())
                .title(blog.getTitle())
                .body(blog.getBody())
                .authorId(blog.getAuthorId().toString())
                .status(blog.getStatus().toString())
                .createdAt(blog.getCreatedAt())
                .savedAt(blog.getSavedAt())
                .published(PublishedBlogPO.of(blog.getPublished()))
                .build();
    }
}
