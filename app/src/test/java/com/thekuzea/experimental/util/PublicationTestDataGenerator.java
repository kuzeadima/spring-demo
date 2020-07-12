package com.thekuzea.experimental.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.domain.dto.PublicationDto;
import com.thekuzea.experimental.domain.model.Publication;

import static com.thekuzea.experimental.util.UserTestDataGenerator.createUser;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublicationTestDataGenerator {

    public static Publication createPublication() {
        return Publication.builder()
                .id(UUID.fromString("95377ef8-3010-45a9-a5f7-296b98f314b6"))
                .publishedBy(createUser())
                .publicationTime(DateTimeUtil.convertStringToOffsetDateTime("2020-04-26T11:48:54.758+02:00"))
                .topic("Hieracium venosum L.")
                .body("Integer ac neque. Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh.")
                .build();
    }

    public static List<Publication> createPublicationList() {
        final Publication publication1 = Publication.builder()
                .id(UUID.fromString("95377ef8-3010-45a9-a5f7-296b98f314b6"))
                .publishedBy(createUser())
                .publicationTime(DateTimeUtil.convertStringToOffsetDateTime("2020-04-26T11:48:54.758+02:00"))
                .topic("Hieracium venosum L.")
                .body("Integer ac neque. Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh.")
                .build();

        final Publication publication2 = Publication.builder()
                .id(UUID.fromString("feea949a-f2f3-44ec-b861-0bb521fd7b54"))
                .publishedBy(createUser())
                .publicationTime(DateTimeUtil.convertStringToOffsetDateTime("2020-03-14T17:41:34.651+02:00"))
                .topic("Carex adusta Boott")
                .body("Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci. Mauris lacinia sapien quis libero. Nullam sit amet turpis elementum ligula vehicula consequat. Morbi a ipsum. Integer a nibh. In quis justo. Maecenas rhoncus aliquam lacus.")
                .build();

        final Publication publication3 = Publication.builder()
                .id(UUID.fromString("b339b3f6-1826-4b62-9f37-742339310075"))
                .publishedBy(createUser())
                .publicationTime(DateTimeUtil.convertStringToOffsetDateTime("2020-01-29T06:27:59.398+02:00"))
                .topic("Collinsia heterophylla Buist ex Graham")
                .body("Maecenas ut massa quis augue luctus tincidunt. Nulla mollis molestie lorem. Quisque ut erat. Curabitur gravida nisi at nibh. In hac habitasse platea dictumst.")
                .build();

        return Arrays.asList(publication1, publication2, publication3);
    }

    public static PublicationDto createPublicationResource() {
        return PublicationDto.builder()
                .id("95377ef8-3010-45a9-a5f7-296b98f314b6")
                .publicationTime("2020-04-26T11:48:54.758+02:00")
                .publishedBy("Larry")
                .topic("Hieracium venosum L.")
                .body("Integer ac neque. Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh.")
                .build();
    }

    public static PublicationDto createPublicationResourceAsNewPublication() {
        return PublicationDto.builder()
                .publicationTime("2020-02-26T14:21:43.298+02:00")
                .topic("Achillea alpina L.")
                .body("In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem. Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat.")
                .build();
    }

    public static List<PublicationDto> createPublicationResourceList() {
        final String otherUser = "Kate";
        final PublicationDto publicationDto1 = PublicationDto.builder()
                .id("95377ef8-3010-45a9-a5f7-296b98f314b6")
                .publishedBy(createUser().getUsername())
                .publicationTime("2020-04-26T11:48:54.758+02:00")
                .topic("Hieracium venosum L.")
                .body("Integer ac neque. Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh.")
                .build();

        final PublicationDto publicationDto2 = PublicationDto.builder()
                .id("feea949a-f2f3-44ec-b861-0bb521fd7b54")
                .publishedBy(otherUser)
                .publicationTime("2020-03-14T17:41:34.651+02:00")
                .topic("Carex adusta Boott")
                .body("Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci. Mauris lacinia sapien quis libero. Nullam sit amet turpis elementum ligula vehicula consequat. Morbi a ipsum. Integer a nibh. In quis justo. Maecenas rhoncus aliquam lacus.")
                .build();

        final PublicationDto publicationDto3 = PublicationDto.builder()
                .id("b339b3f6-1826-4b62-9f37-742339310075")
                .publishedBy(otherUser)
                .publicationTime("2020-01-29T06:27:59.398+02:00")
                .topic("Collinsia heterophylla Buist ex Graham")
                .body("Maecenas ut massa quis augue luctus tincidunt. Nulla mollis molestie lorem. Quisque ut erat. Curabitur gravida nisi at nibh. In hac habitasse platea dictumst.")
                .build();

        return Arrays.asList(publicationDto1, publicationDto2, publicationDto3);
    }

    public static List<PublicationDto> createPublicationResourceListForCurrentUser() {
        final String publishedBy = "Kate";
        final PublicationDto publicationDto1 = PublicationDto.builder()
                .id("feea949a-f2f3-44ec-b861-0bb521fd7b54")
                .publishedBy(publishedBy)
                .publicationTime("2020-03-14T17:41:34.651+02:00")
                .topic("Carex adusta Boott")
                .body("Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci. Mauris lacinia sapien quis libero. Nullam sit amet turpis elementum ligula vehicula consequat. Morbi a ipsum. Integer a nibh. In quis justo. Maecenas rhoncus aliquam lacus.")
                .build();

        final PublicationDto publicationDto2 = PublicationDto.builder()
                .id("b339b3f6-1826-4b62-9f37-742339310075")
                .publishedBy(publishedBy)
                .publicationTime("2020-01-29T06:27:59.398+02:00")
                .topic("Collinsia heterophylla Buist ex Graham")
                .body("Maecenas ut massa quis augue luctus tincidunt. Nulla mollis molestie lorem. Quisque ut erat. Curabitur gravida nisi at nibh. In hac habitasse platea dictumst.")
                .build();

        return Arrays.asList(publicationDto1, publicationDto2);
    }

    public static PublicationDto createPublicationResourceAsNewPublicationWrongPublisher() {
        return PublicationDto.builder()
                .publicationTime("2020-04-13T03:41:16.298+02:00")
                .topic("Achillea alpina L.")
                .body("In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem. Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat.")
                .build();
    }

    public static PublicationDto createPublicationResourceAsNewPublicationWhereItAlreadyExists() {
        return PublicationDto.builder()
                .publicationTime("2020-04-13T03:41:16.298+02:00")
                .topic("Carex adusta Boott")
                .body("In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem. Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat.")
                .build();
    }
}
