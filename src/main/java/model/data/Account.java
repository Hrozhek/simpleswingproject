package model.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public final class Account {
    @EqualsAndHashCode.Exclude private final Long id;
    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
