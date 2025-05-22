package com.micro.commonlib.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> implements Serializable {
    private int currentPage;
    private int totalPage;
    private int pageSize;
    private long totalElements;

    @Builder.Default
    private List<T> data = Collections.emptyList();

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}

/*
int pageIndex = Math.max(page - 1, 0);
Pageable pageable = PageRequest.of(pageIndex, size, sort);

return PageResponse.<UserResponse>builder()
                .currentPage(pageData.getNumber() + 1)
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())

 */