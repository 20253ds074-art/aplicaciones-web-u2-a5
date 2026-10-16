package mx.edu.utez.proyecto1C.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class RequestBodyDTO {
    private String nombre;
    private int edad;


}
