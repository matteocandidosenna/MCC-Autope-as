import java.sql.*;
import java.util.*;

public class Venda {

    private Connection conn;

    // Construtor: inicializa a conexão
    public Venda() throws SQLException {
        this.conn = Conexao.getConnection();
    }

    public void gerarVenda() {
        Scanner sc = new Scanner(System.in);

        try {

            // =====================
            // 1. Seleção de serviços
            // =====================
            List<Integer> servicosSelecionados = new ArrayList<>();
            System.out.println("\n=== LISTA DE SERVIÇOS ===");
            Statement stmtServ = conn.createStatement();
            ResultSet rsServ = stmtServ.executeQuery("SELECT * FROM Servicos");

            while (rsServ.next()) {
                System.out.println(rsServ.getInt("id_servico") + " - " +
                        rsServ.getString("nome_servico") + " - R$" +
                        rsServ.getDouble("preco_servico"));
            }
            rsServ.close();
            stmtServ.close();

            System.out.println("Digite os IDs dos serviços desejados (0 para parar): ");
            while (true) {
                int id = sc.nextInt();
                if (id == 0) break;
                servicosSelecionados.add(id);
            }

            // =====================
            // 2. Seleção de produtos
            // =====================
            List<Integer> produtosSelecionados = new ArrayList<>();
            System.out.println("\n=== LISTA DE PRODUTOS ===");
            Statement stmtProd = conn.createStatement();
            ResultSet rsProd = stmtProd.executeQuery("SELECT * FROM Produtos");

            while (rsProd.next()) {
                System.out.println(rsProd.getInt("id_produto") + " - " +
                        rsProd.getString("nome_produto") + " (" +
                        rsProd.getString("marca_produto") + ") - R$" +
                        rsProd.getDouble("preco_produto"));
            }
            rsProd.close();
            stmtProd.close();

            System.out.println("Digite os IDs dos produtos desejados (0 para parar): ");
            while (true) {
                int id = sc.nextInt();
                if (id == 0) break;
                produtosSelecionados.add(id);
            }

            // =====================
            // 3. Seleção de cliente
            // =====================
            System.out.println("\n=== LISTA DE CLIENTES ===");
            Statement stmtCli = conn.createStatement();
            ResultSet rsCli = stmtCli.executeQuery("SELECT * FROM Cliente");

            while (rsCli.next()) {
                System.out.println(rsCli.getInt("id_cliente") + " - " +
                        rsCli.getString("nome") + " - CPF: " +
                        rsCli.getString("cpf"));
            }
            rsCli.close();
            stmtCli.close();

            System.out.print("Digite o ID do cliente: ");
            int idCliente = sc.nextInt();

            // =====================
            // 4. Seleção de funcionário
            // =====================
            System.out.println("\n=== LISTA DE FUNCIONÁRIOS ===");
            Statement stmtFunc = conn.createStatement();
            ResultSet rsFunc = stmtFunc.executeQuery("SELECT * FROM Funcionario");

            while (rsFunc.next()) {
                System.out.println(rsFunc.getInt("id_funcionario") + " - " +
                        rsFunc.getString("nome") + " - Cargo: " +
                        rsFunc.getString("cargo"));
            }
            rsFunc.close();
            stmtFunc.close();

            System.out.print("Digite o ID do funcionário: ");
            int idFuncionario = sc.nextInt();

            // =====================
            // 5. Calcular valor total
            // =====================
            double valorTotal = 0;

            for (int id : servicosSelecionados) {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT preco_servico FROM Servicos WHERE id_servico = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) valorTotal += rs.getDouble("preco_servico");
                rs.close();
                ps.close();
            }

            for (int id : produtosSelecionados) {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT preco_produto FROM Produtos WHERE id_produto = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) valorTotal += rs.getDouble("preco_produto");
                rs.close();
                ps.close();
            }

            // =====================
            // 6. Inserir na tabela Venda
            // =====================
            String produtosTxt = produtosSelecionados.toString();
            String servicosTxt = servicosSelecionados.toString();

            String sqlVenda = "INSERT INTO Venda (data_venda, id_cliente, id_funcionario, valor_total, produtos, servicos) " +
                    "VALUES (CURDATE(), ?, ?, ?, ?, ?)";

            PreparedStatement psVenda = conn.prepareStatement(sqlVenda);
            psVenda.setInt(1, idCliente);
            psVenda.setInt(2, idFuncionario);
            psVenda.setDouble(3, valorTotal);
            psVenda.setString(4, produtosTxt);
            psVenda.setString(5, servicosTxt);

            psVenda.executeUpdate();
            psVenda.close();

            System.out.println("\n✅ Venda gerada com sucesso!");
            System.out.println("Cliente ID: " + idCliente);
            System.out.println("Funcionário ID: " + idFuncionario);
            System.out.println("Produtos: " + produtosTxt);
            System.out.println("Serviços: " + servicosTxt);
            System.out.println("Valor Total: R$" + valorTotal);

        } catch (SQLException e) {
            System.err.println("Erro ao gerar venda: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    public void listarVendas() throws SQLException {
        String sql = "SELECT v.id_venda, v.data_venda, v.valor_total, " +
                "c.nome AS cliente, f.nome AS funcionario, " +
                "v.produtos, v.servicos " +
                "FROM Venda v " +
                "JOIN Cliente c ON v.id_cliente = c.id_cliente " +
                "JOIN Funcionario f ON v.id_funcionario = f.id_funcionario";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\n=== Lista de Vendas ===");
        while (rs.next()) {
            int idVenda = rs.getInt("id_venda");
            Date dataVenda = rs.getDate("data_venda");
            String cliente = rs.getString("cliente");
            String funcionario = rs.getString("funcionario");
            double valorTotal = rs.getDouble("valor_total");

            String produtosTxt = rs.getString("produtos");
            String servicosTxt = rs.getString("servicos");

            String nomesProdutos = recuperarNomes("Produtos", "id_produto", "nome_produto", produtosTxt);
            String nomesServicos = recuperarNomes("Servicos", "id_servico", "nome_servico", servicosTxt);

            System.out.println("ID Venda: " + idVenda +
                    " | Data: " + dataVenda +
                    " | Cliente: " + cliente +
                    " | Funcionário: " + funcionario +
                    " | Produtos: " + nomesProdutos +
                    " | Serviços: " + nomesServicos +
                    " | Valor Total: R$ " + valorTotal);
        }

        rs.close();
        stmt.close();
    }

    private String recuperarNomes(String tabela, String colunaId, String colunaNome, String idsTxt) throws SQLException {
        if (idsTxt == null || idsTxt.trim().equals("[]")) return "Nenhum";

        idsTxt = idsTxt.replace("[", "").replace("]", "").trim();
        if (idsTxt.isEmpty()) return "Nenhum";

        String[] ids = idsTxt.split(",");
        StringBuilder nomes = new StringBuilder();

        for (String idStr : ids) {
            int id = Integer.parseInt(idStr.trim());
            String sql = "SELECT " + colunaNome + " FROM " + tabela + " WHERE " + colunaId + " = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (nomes.length() > 0) nomes.append(", ");
                nomes.append(rs.getString(colunaNome));
            }
            rs.close();
            stmt.close();
        }

        return nomes.toString();
    }
}
