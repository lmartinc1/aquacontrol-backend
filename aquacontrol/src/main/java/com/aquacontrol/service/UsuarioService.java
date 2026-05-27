package com.aquacontrol.service;

import com.aquacontrol.entity.Usuario;
import com.aquacontrol.exception.BusinessException;
import com.aquacontrol.exception.ResourceNotFoundException;
import com.aquacontrol.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
    }

    @Transactional
    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }
        // Hash de contraseña con BCrypt
        usuario.setCredenciales(passwordEncoder.encode(usuario.getCredenciales()));
        usuario.setEstado(Usuario.EstadoUsuario.ACTIVO);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(Long id, Usuario datos) {
        Usuario usuario = obtenerPorId(id);
        usuario.setNombre(datos.getNombre());
        usuario.setRol(datos.getRol());
        if (datos.getHogar() != null) usuario.setHogar(datos.getHogar());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario cambiarEstado(Long id, Usuario.EstadoUsuario estado) {
        Usuario usuario = obtenerPorId(id);
        usuario.setEstado(estado);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void cambiarPassword(Long id, String nuevaPassword) {
        Usuario usuario = obtenerPorId(id);
        usuario.setCredenciales(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }
}
