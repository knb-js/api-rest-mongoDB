package com.mongodb.apirestmongodb.controller;

import com.mongodb.apirestmongodb.entity.User;
import com.mongodb.apirestmongodb.repository.UserRepository;
import com.mongodb.apirestmongodb.response.ResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
@Log4j2
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<ResponseDTO> findAllUsers() {
        ResponseDTO.ResponseDTOBuilder respBuilder = ResponseDTO.builder();
        try {
            List<User> users = userRepository.findAll();
            boolean success = Objects.nonNull(users);
            respBuilder
                    .status(success)
                    .message(success ? "Usuarios encontrados con éxito" : "Ocurrió un error al obtener los usuario")
                    .data(users);
            return new ResponseEntity<>(respBuilder.build(), success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            log.error("Error al obtener el usuario: " + e.getMessage());
            respBuilder
                    .status(false)
                    .message("Error al obtener usuario: " + e.getMessage());
            return new ResponseEntity<>(respBuilder.build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody User user) {
        ResponseDTO.ResponseDTOBuilder respBuilder = ResponseDTO.builder();
        try {
            User userCreate = userRepository.save(user);
            boolean success = Objects.nonNull(userCreate);
            respBuilder
                    .status(success)
                    .message(success ? "Usuario registrado con éxito" : "Ocurrió un error al registrar el usuario")
                    .data(userCreate);
            return new ResponseEntity<>(respBuilder.build(), success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            log.error("Error al registrar usuario: " + e.getMessage());
            respBuilder
                    .status(false)
                    .message("Error al registrar usuario: " + e.getMessage());
            return new ResponseEntity<>(respBuilder.build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable("id") String id, @RequestBody User user) {
        ResponseDTO.ResponseDTOBuilder respBuilder = ResponseDTO.builder();
        try {
            if (!userRepository.existsById(Integer.valueOf(id))) {
                respBuilder
                        .status(false)
                        .message("Usuario no encontrado");
                return new ResponseEntity<>(respBuilder.build(), HttpStatus.NOT_FOUND);
            }

            user.setId(Integer.valueOf(id));
            User updatedUser = userRepository.save(user);
            boolean success = Objects.nonNull(updatedUser);

            respBuilder
                    .status(success)
                    .message(success ? "Usuario actualizado con éxito" : "Ocurrió un error al actualizar el usuario")
                    .data(updatedUser);

            return new ResponseEntity<>(respBuilder.build(), success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            log.error("Error al actualizar el usuario: " + e.getMessage());
            respBuilder
                    .status(false)
                    .message("Error al actualizar el usuario: " + e.getMessage());
            return new ResponseEntity<>(respBuilder.build(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletedUser/{id}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable("id") Integer id) {
        ResponseDTO.ResponseDTOBuilder respBuilder = ResponseDTO.builder();
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                respBuilder
                        .status(true)
                        .message("Usuario eliminado con éxito");
                return new ResponseEntity<>(respBuilder.build(), HttpStatus.OK);
            } else {
                respBuilder
                        .status(false)
                        .message("Usuario no encontrado");
                return new ResponseEntity<>(respBuilder.build(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error al eliminar el usuario: " + e.getMessage());
            respBuilder
                    .status(false)
                    .message("Error al eliminar el usuario: " + e.getMessage());
            return new ResponseEntity<>(respBuilder.build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
