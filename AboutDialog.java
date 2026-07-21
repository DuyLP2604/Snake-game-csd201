import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class AboutDialog extends JDialog {
    public AboutDialog(JFrame parent) {

        super(parent, "About", true);

        setSize(600, 450);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20,20,30));
        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,20,20,20));

        String[] columns = {
                "Name",
                "Student ID",
                "Role"
        };

        String[][] data = {
                {"Trương Gia Hân", "CE200068", "Leader"},
                {"Lê Phước Duy", "CE201148", "Member"},
                {"Nguyễn Tấn Lợi", "CE201149", "Member"},
                {"Võ Nguyễn Thái Dương", "CE201235", "Member"},
                {"Nguyễn Hồ Quốc Duy", "CE170225", "Member"}
        };
        JTable table = new JTable(data, columns);

        table.setFont(new Font("Consolas", Font.PLAIN, 14));
        table.setRowHeight(28);

        table.setBackground(new Color(30,30,45));
        table.setForeground(Color.WHITE);

        table.setGridColor(new Color(0,255,150));

        table.getTableHeader().setFont(
                new Font("Consolas", Font.BOLD, 14)
        );

        table.getTableHeader().setBackground(
                new Color(20,20,30)
        );

        table.getTableHeader().setForeground(
                new Color(0,255,150)
        );

        table.setEnabled(false);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        JTextArea projectInfo = new JTextArea(
                """
                Snake Game OOP

                FPT University
                Class: SE2001
                Mentor: LanTT

                CSD201 Final Project
                by Group 2
                """
        );

        projectInfo.setEditable(false);
        projectInfo.setOpaque(false);
        projectInfo.setForeground(Color.WHITE);
        projectInfo.setFont(
                new Font("Consolas", Font.PLAIN, 17)
        );

        JPanel box = new JPanel(new BorderLayout());

        box.setBackground(
                new Color(30,30,45)
        );

        box.setBorder(
                BorderFactory.createTitledBorder(
                        new LineBorder(
                                new Color(0,255,150),2),
                        "ABOUT",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font(
                                "Consolas",
                                Font.BOLD,
                                16),
                        new Color(0,255,150)
                )
        );

        box.add(projectInfo, BorderLayout.NORTH);
        box.add(table.getTableHeader(), BorderLayout.CENTER);
        box.add(table, BorderLayout.SOUTH);

        panel.add(box, BorderLayout.CENTER);

        add(panel);
    }
}
