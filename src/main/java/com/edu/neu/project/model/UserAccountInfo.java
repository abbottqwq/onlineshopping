package com.edu.neu.project.model;

import com.edu.neu.project.validationannotation.MatchPassword;
import com.edu.neu.project.validationannotation.ValidNameInput;
import com.edu.neu.project.validationannotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@MatchPassword
public class UserAccountInfo {
    @ValidNameInput
    private String username;
    @ValidPassword
    private String password;
    private String retypePassword;
    @ValidNameInput
    private String displayName;

}
