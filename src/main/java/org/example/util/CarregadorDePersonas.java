package util;

import org.example.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class CarregadorDePersonas {

    public static List<Usuario> carregar() {
        List<Usuario> usuarios = new ArrayList<>();

        // 1.baixa familiaridade digital
        usuarios.add(new Usuario(1L, "Seu Joao (Aposentado)", "joao@email.com", false));
        usuarios.add(new Usuario(2L, "Dona Maria (Diarista)", "maria@email.com", false));
        usuarios.add(new Usuario(3L, "Carlos (Construcao Civil)", "carlos@email.com", false));

        // 2.vulnerabilidade
        usuarios.add(new Usuario(4L, "Ana (Vitima Violencia Domestica)", "ana@email.com", false));
        usuarios.add(new Usuario(5L, "Marcos (Comerciante Local)", "marcos@email.com", false));
        usuarios.add(new Usuario(6L, "Lucia (Terceirizada)", "lucia@email.com", false));

        // 3.gestores
        usuarios.add(new Usuario(7L, "Roberto (Triagem)", "roberto.gov@email.com", false));
        usuarios.add(new Usuario(8L, "Helena (Gestora Zeladoria)", "helena.gov@email.com", false));
        usuarios.add(new Usuario(9L, "Fernando (Ouvidor Geral)", "fernando.gov@email.com", false));

        usuarios.add(new Usuario(99L, "CIDADAO ANONIMO", "oculto", true));

        return usuarios;
    }
}