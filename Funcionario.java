import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Funcionario {

    // Inserir
    public void inserirFuncionario(String cpf, String nome, String cargo, double salario, String telefone) {
        String sql = "INSERT INTO Funcionario (cpf, nome, cargo, salario, telefone) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.setString(2, nome);
            stmt.setString(3, cargo);
            stmt.setDouble(4, salario);
            stmt.setString(5, telefone);
            stmt.executeUpdate();

            System.out.println("✅ Funcionário inserido com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir funcionário: " + e.getMessage());
        }
    }

    // Excluir
    public void excluirFuncionario(int idFuncionario) {
        String sql = "DELETE FROM Funcionario WHERE id_funcionario = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("✅ Funcionário excluído com sucesso!");
            } else {
                System.out.println("⚠ Nenhum funcionário encontrado com esse ID.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir funcionário: " + e.getMessage());
        }
    }

    // Listar
    public List<String> listarFuncionarios() {
        List<String> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionario";

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String funcionario = rs.getInt("id_funcionario") + " - " +
                                     rs.getString("nome") + " - " +
                                     rs.getString("cpf") + " - " +
                                     rs.getString("cargo") + " - " +
                                     rs.getDouble("salario") + " - " +
                                     rs.getString("telefone");
                funcionarios.add(funcionario);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
        }

        return funcionarios;
    }
}
