package mx.edu.utez.proyecto1C.controller;

import mx.edu.utez.proyecto1C.controller.dto.RequestBodyDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin({"*"}) //todos los origenes de peticiones
@RequestMapping("/my-services")


public class MyController {
    @GetMapping
    public String miPrimerServicio(){
        return "Hello world";
    }

    @GetMapping("/servicio2")
    public String servicio2(){
        return "segundo servicio";
    }
    @PostMapping
    public String servicip3(){
        return "Este es el servicio 3";
    }

    ///
    @GetMapping("/path/{id}")
    public String pathvariable(@PathVariable String id){
        return "El path variable es " + id;

    }
    @PostMapping("/body")
    public String body(@RequestBody RequestBodyDTO payload){
        System.out.println(payload.getEdad());
        System.out.println(payload.getNombre());

        return "Este es el servicio 4";
    }


}
