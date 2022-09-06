package com.sistema.examenes.controladores;

import com.sistema.examenes.configuraciones.JwtUtils;
import com.sistema.examenes.entidades.JwtRequest;
import com.sistema.examenes.entidades.JwtResponse;
import com.sistema.examenes.entidades.Usuario;
import com.sistema.examenes.excepciones.UsuarioNotFoundException;
import com.sistema.examenes.servicios.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/generate-token")
    public ResponseEntity<?> generarToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            auntenticar(jwtRequest.getUsername(),jwtRequest.getPassword());
        } catch (UsuarioNotFoundException exception) {
            exception.printStackTrace();
            throw new Exception("Usuario no encontrado");

        }

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void auntenticar(String username, String password) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        }catch (DisabledException disabledException){
            throw new Exception("USUARIO DESABILITADO"+ disabledException.getMessage());
        }catch (BadCredentialsException badCredentialsException){
            throw new Exception("Credenciales Invalidas"+ badCredentialsException.getMessage());
        }
    }

    @GetMapping("/actual-usuario")
    public Usuario obtenerUsuarioActual(Principal principal){
        return (Usuario) this.userDetailsService.loadUserByUsername(principal.getName());
    }
}
