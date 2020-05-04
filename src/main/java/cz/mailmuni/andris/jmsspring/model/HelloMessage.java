package cz.mailmuni.andris.jmsspring.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelloMessage implements Serializable {

    static final long serialVersionUID = -1552651783409477361L;

    private UUID id;
    private String message;

}
