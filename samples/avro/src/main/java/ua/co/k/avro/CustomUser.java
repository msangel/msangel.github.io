package ua.co.k.avro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.avro.reflect.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUser {
    private String name;
    private Integer favoriteNumber;
    @Nullable
    private String favoriteColor;
}
