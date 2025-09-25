import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente {

    // Inserir
    public void inserirCliente(String nome, String cpf, String telefone) {
        String sql = "INSERT INTO Cliente (nome, cpf, telefone) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, telefone);
            stmt.executeUpdate();

            System.out.println("✅ Cliente inserido com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente: " + e.getMessage());
        }
    }

    // Excluir
    public void excluirCliente(int idCliente) {
        String sql = "DELETE FROM Cliente WHERE id_cliente = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("✅ Cliente excluído com sucesso!");
            } else {
                System.out.println("⚠ Nenhum cliente encontrado com esse ID.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir cliente: " + e.getMessage());
        }
    }

    // Listar
    public List<String> listarClientes() {
        List<String> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String cliente = rs.getInt("id_cliente") + " - " +
                                 rs.getString("nome") + " - " +
                                 rs.getString("cpf") + " - " +
                                 rs.getString("telefone");
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
        }

        return clientes;
    }
}
