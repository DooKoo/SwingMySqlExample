package com.company;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;


/**
 * Created by DooKoo on 17.06.2014.
 */
public class View extends JFrame {

    // Buttons.
    private JButton CalculateButton = new JButton("Calculate");

    // Fields for input data.
    private JTextField AInputName= new JTextField(10);
    private JTextField BInputName= new JTextField(10);

    // Layouts.
    private JFrame MainFrame = new JFrame("Window");
    private JPanel ButtonPanel = new JPanel(new FlowLayout());
    private JPanel FieldPanel = new JPanel(new FlowLayout());
    private JPanel TablePanel = new JPanel(new FlowLayout());

    // Table for view data from the collection.
    private TableModel MainTableModel = new TableModel();
    private JTable Table = new JTable(MainTableModel);
    private JScrollPane TableScrollPane = new JScrollPane(Table);

    public View()
    {
        MainFrame.setLayout(new FlowLayout());
        MainFrame.setSize(600,600);
        MainFrame.setVisible(true);
        MainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ButtonPanel.add(CalculateButton);
        FieldPanel.add(AInputName);
        FieldPanel.add(BInputName);

        // Adds element to TablePanel.
        TablePanel.add(TableScrollPane);

        // Adds layouts to MainFrame.
        MainFrame.add(FieldPanel);
        MainFrame.add(ButtonPanel);
        MainFrame.add(TablePanel);

        CalculateButton.addActionListener((e) -> {
                PreparedStatement pst;
                MyDBConnection mdbcon;
                Connection conn;
                try{
                    String sql="INSERT INTO test(A, B) " +
                            "VALUES (" + AInputName.getText() + ", " + BInputName.getText() + ")";
                    mdbcon= new MyDBConnection();
                    conn=mdbcon.getMyConnection();

                    pst = conn.prepareStatement(sql);
                    pst.executeUpdate();
                    pst.close();
                }catch(SQLException exception){exception.printStackTrace();}
                ((TableModel)Table.getModel()).refresh();
                Table.updateUI();
            });
    }
    private  class TableModel extends AbstractTableModel {
        LinkedList<Integer> AData = new LinkedList<>();
        LinkedList<Integer> BData = new LinkedList<>();
        LinkedList<Integer> ResultData = new LinkedList<>();
        public TableModel(){
            refresh();
        }

        public void refresh(){

            PreparedStatement pst;
            MyDBConnection mdbcon;
            Connection conn;
            ResultSet rs;
            try{
                String sql="SELECT * FROM test";
                mdbcon= new MyDBConnection();
                conn=mdbcon.getMyConnection();

                pst = conn.prepareStatement(sql);
                rs=pst.executeQuery();
                AData.clear();
                BData.clear();
                ResultData.clear();
                while (rs.next())
                {
                    int a=rs.getInt("a");
                    int b=rs.getInt("b");

                    AData.add(a);
                    BData.add(b);
                    ResultData.add(a+b);
                }
                mdbcon.close(rs);
            }catch(SQLException e){e.printStackTrace();}
        }

        @Override
        public int getRowCount() {
            return ResultData.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return AData.get(rowIndex);
                case 1:
                    return BData.get(rowIndex);
                case 2:
                    return ResultData.get(rowIndex);
                default:
                    return null;
            }
        }
    }

}