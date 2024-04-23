package filmoteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class PosudiFilm {
    public JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboBoxMovies;
    private JComboBox<String> comboBoxClan;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PosudiFilm window = new PosudiFilm();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PosudiFilm() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(135, 206, 250));
        frame.setBounds(100, 100, 542, 529);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Odaberite film:");
        lblNewLabel.setFont(new Font("Shrikhand", Font.PLAIN, 20));
        lblNewLabel.setBounds(183, 0, 161, 55);
        frame.getContentPane().add(lblNewLabel);

        comboBoxMovies = new JComboBox<>();
        comboBoxMovies.setFont(new Font("Shrikhand", Font.PLAIN, 20));
        comboBoxMovies.setBounds(113, 54, 285, 55);
        frame.getContentPane().add(comboBoxMovies);

        filmoviComboBox();
        
        comboBoxClan = new JComboBox<>();
        comboBoxClan.setFont(new Font("Shrikhand", Font.PLAIN, 20));
        comboBoxClan.setBounds(113, 138, 285, 55);
        frame.getContentPane().add(comboBoxClan);

        clanoviComboBox();

        JButton btnNewButton = new JButton("Posudi");
        btnNewButton.setForeground(new Color(255, 255, 255));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFilm = (String) comboBoxMovies.getSelectedItem();
                String selectedClan = (String) comboBoxClan.getSelectedItem();
                if (selectedFilm != null && selectedClan != null) {
                    iznajmiFilm(selectedFilm, selectedClan);
                } else {
                    JOptionPane.showMessageDialog(null, "Odaberite i film i člana.");
                }
            }
        });
        
        btnNewButton.setBackground(new Color(255, 0, 0));
        btnNewButton.setFont(new Font("Shrikhand", Font.PLAIN, 20));
        btnNewButton.setBounds(183, 224, 154, 39);
        frame.getContentPane().add(btnNewButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(97, 274, 320, 158);
        frame.getContentPane().add(scrollPane);

        tableModel = new DefaultTableModel(); 
        table = new JTable(tableModel);       
        scrollPane.setViewportView(table);
        tableModel.addColumn("Posuđeni film");
        tableModel.addColumn("Ime clana");

        JButton btnVracenFilm = new JButton("Vraćen film");
        btnVracenFilm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int odabraniRedak = table.getSelectedRow();

                if (odabraniRedak >= 0) {
                    String nazivFilma = (String) table.getValueAt(odabraniRedak, 0);
                    String imeClana = (String) table.getValueAt(odabraniRedak, 1);

                    if (nazivFilma != null && !nazivFilma.isEmpty() && imeClana != null && !imeClana.isEmpty()) {
                        try {

                            int filmId = dohvatiFilmId(nazivFilma);
                            int clanId = dohvatiClanId(imeClana);

                            Connection con = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/dmedak?serverTimezone=UTC", "dmedak", "19");

                            String deleteQuery = "DELETE FROM Iznajmljeni_filmovi WHERE film_id = ? AND clan_id = ?";
                            PreparedStatement ps = con.prepareStatement(deleteQuery);
                            ps.setInt(1, filmId);
                            ps.setInt(2, clanId);
                            ps.executeUpdate();

                            tableModel.removeRow(odabraniRedak);
                            JOptionPane.showMessageDialog(null, "Uspješno vraćen film!");

                            con.close();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Neuspješno brisanje filma iz baze!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Neispravni podaci u odabranom redu");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nije odabran redak za vraćanje filma");
                }
            }
        });


        btnVracenFilm.setForeground(Color.WHITE);
        btnVracenFilm.setFont(new Font("Shrikhand", Font.PLAIN, 20));
        btnVracenFilm.setBackground(Color.RED);
        btnVracenFilm.setBounds(166, 444, 183, 39);
        frame.getContentPane().add(btnVracenFilm);

        
        JLabel clan = new JLabel("Tko posuđuje:");
        clan.setFont(new Font("Dialog", Font.PLAIN, 20));
        clan.setBounds(183, 103, 161, 39);
        frame.getContentPane().add(clan);
    }

    private void filmoviComboBox() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/dmedak?serverTimezone=UTC", "dmedak", "19");

            String query = "SELECT naziv_filma FROM Filmovi";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            List<String> movies = new ArrayList<>();
            while (rs.next()) {
                movies.add(rs.getString("naziv_filma"));
            }
            con.close();
            comboBoxMovies.setModel(new DefaultComboBoxModel<>(movies.toArray(new String[0])));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void clanoviComboBox() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/dmedak?serverTimezone=UTC", "dmedak", "19");

            String query = "SELECT ime_clana FROM Clanovi";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            List<String> clanovi = new ArrayList<>();
            while (rs.next()) {
                clanovi.add(rs.getString("ime_clana"));
            }
            con.close();
            comboBoxClan.setModel(new DefaultComboBoxModel<>(clanovi.toArray(new String[0])));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int dohvatiFilmId(String nazivFilma) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/dmedak?serverTimezone=UTC", "dmedak", "19");
            String selectFilmIdQuery = "SELECT film_id FROM Filmovi WHERE naziv_filma = ?";
            PreparedStatement filmIdPs = con.prepareStatement(selectFilmIdQuery);
            filmIdPs.setString(1, nazivFilma);
            ResultSet filmId = filmIdPs.executeQuery();

            if (filmId.next()) {
                return filmId.getInt("film_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int dohvatiClanId(String imeClana) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/dmedak?serverTimezone=UTC", "dmedak", "19");
            String selectClanIdQuery = "SELECT clan_id FROM Clanovi WHERE ime_clana = ?";
            PreparedStatement clanIdPs = con.prepareStatement(selectClanIdQuery);
            clanIdPs.setString(1, imeClana);
            ResultSet clanId = clanIdPs.executeQuery();

            if (clanId.next()) {
                return clanId.getInt("clan_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; 
    }

    private void iznajmiFilm(String nazivFilma, String imeClana) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/dmedak?serverTimezone=UTC", "dmedak", "19");
            
            int filmId = dohvatiFilmId(nazivFilma);
            int clanId = dohvatiClanId(imeClana);

            if (filmId != -1 && clanId != -1) {
                String insertQuery = "INSERT INTO Iznajmljeni_filmovi (film_id, clan_id) VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(insertQuery);
                ps.setInt(1, filmId);
                ps.setInt(2, clanId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Film uspješno posuđen.");
                comboBoxMovies.removeItem(nazivFilma);
                tableModel.addRow(new Object[]{nazivFilma, imeClana});
            } else {
                JOptionPane.showMessageDialog(null, "Neuspješno dohvaćanje film_id ili clan_id.");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

