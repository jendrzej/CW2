import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mateusz on 2016-05-10.
 */
public class Okienko extends JFrame {
    private JTextField txtTST;
    private JButton btnTST;
    private JPanel rootPanel;
    private JButton btnTRN;
    private JTextField txtTRN;
    private JComboBox cbMetryka;
    private JSpinner k;
    private JButton btnGeneruj;

    public Okienko(){
        setContentPane(rootPanel);
        setVisible(true);
        setSize(640,480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cbMetryka.addItem("Czebyszewa");
        cbMetryka.addItem("Euklidesowa");
        cbMetryka.addItem("Manhattan");
        cbMetryka.addItem("Canberra");
        cbMetryka.addItem("Pearsona");

        txtTRN.setEditable(false);
        txtTST.setEditable(false);
        k.setEnabled(false);
        btnGeneruj.setEnabled(false);


        btnTST.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc= new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("txt","txt"));
                fc.setAcceptAllFileFilterUsed(false);
                if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                    txtTST.setText(fc.getSelectedFile().getAbsolutePath());

                }
            }
        });

        btnTRN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc= new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("txt","txt"));
                fc.setAcceptAllFileFilterUsed(false);
                if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                    txtTRN.setText((fc.getSelectedFile().getAbsolutePath()));
                    SpinnerNumberModel model=new SpinnerNumberModel(1,1,Metryka.obliczMaxK(new SystemDecyzyjny(txtTRN.getText())),1);
                    k.setModel(model);
                    k.setEnabled(true);
                    btnGeneruj.setEnabled(true);



                }
            }
        });



        btnGeneruj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemDecyzyjny tst=new SystemDecyzyjny(txtTST.getText());
                SystemDecyzyjny trn=new SystemDecyzyjny((txtTRN.getText()));
                Metryka m=new Metryka();
               // m.klasyfikujSystem(tst,trn,"Euklidesowa",2);
                JOptionPane.showMessageDialog(null,m.klasyfikujSystem(tst,trn,cbMetryka.getSelectedItem().toString(),(Integer)k.getValue()));

            }
        });



    }
}


