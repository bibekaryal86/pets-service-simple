package pets.service.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest implements Serializable {
    @NonNull
    private String username;
    @NonNull
    @ToString.Exclude
    private String password;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipcode;
    @NonNull
    private String email;
    @NonNull
    private String phone;
    @NonNull
    private String status;
}
