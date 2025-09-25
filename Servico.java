import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {

    // Inserir
    public void inserirServico(String nome, double preco) {
        String sql = "INSERT INTO Servicos (nome_servico, preco_servico) VALUES (?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setDouble(2, preco);
            stmt.executeUpdate();

            System.out.println("✅ Serviço inserido com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir serviço: " + e.getMessage());
        }
    }

    // Excluir
    public void excluirServico(int idServico) {
        String sql = "DELETE FROM Servicos WHERE id_servico = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idServico);
            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("✅ Serviço excluído com sucesso!");
            } else {
                System.out.println("⚠ Nenhum serviço encontrado com esse ID.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir serviço: " + e.getMessage());
        }
    }

    // Listar
    public List<String> listarServicos() {
        List<String> servicos = new ArrayList<>();
        String sql = "SELECT * FROM Servicos";

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String servico = rs.getInt("id_servico") + " - " +
                                 rs.getString("nome_servico") + " - " +
                                 rs.getDouble("preco_servico");
                servicos.add(servico);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar serviços: " + e.getMessage());
        }

        return servicos;
    }
}
