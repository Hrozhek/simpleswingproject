package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class Account {
    private final Long id;
    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
