import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Editor2015 extends JFrame implements ActionListener, KeyListener {
    JFrame jf;
    JMenuBar mb;
    JMenu mFile, mEdit, mFormat, mRun, mHelp;
    JMenuItem mFileMenuItem[] = new JMenuItem[6];
    String StrFileMenuItem[] = { "New", "Open", "Save", "Save As...", "Print...", "Exit" };
    JMenuItem mEditMenuItem[] = new JMenuItem[6];
    String StrEditMenuItem[] = { "Cut", "Copy", "Paste", "Delete", "Select All", "Time & Date" };
    JMenuItem mFormatMenuItem[] = new JMenuItem[1];
    String strFormatMenuItem[] = { "Color" };
    JMenuItem mRunMenuItem[] = new JMenuItem[2];
    String strRunMenuItem[] = { "Compile", "Run.." };

    JTextPane ta;   // styled text editor
    JTextArea ta1;  // output area
    JScrollPane sp, sp1;
    int x;
    FileDialog fd;
    FileInputStream fis;
    FileOutputStream fos;
    byte b[];
    Runtime r;
    String filename = "";
    String fname = "";
    String result = "";
    boolean textchange;
    JColorChooser cc;

    public Editor2015() {
        jf = new JFrame();
        jf.setLayout(null);
        mb = new JMenuBar();
        jf.setJMenuBar(mb);

        mFile = new JMenu("File");
        mb.add(mFile);

        mEdit = new JMenu("Edit");
        mb.add(mEdit);

        mFormat = new JMenu("Format");
        mb.add(mFormat);

        mRun = new JMenu("Run");
        mb.add(mRun);

        mHelp = new JMenu("Help");
        mb.add(mHelp);
        JMenuItem mHelpMenuItem = new JMenuItem("About Editor");
        mHelp.add(mHelpMenuItem);
        mHelpMenuItem.addActionListener(this);

        for (x = 0; x < 6; x++) {
            mFileMenuItem[x] = new JMenuItem(StrFileMenuItem[x]);
            if (x == 4 || x == 5) mFile.addSeparator();
            mFile.add(mFileMenuItem[x]);
            mFileMenuItem[x].addActionListener(this);
        }

        for (x = 0; x < 6; x++) {
            mEditMenuItem[x] = new JMenuItem(StrEditMenuItem[x]);
            if (x == 4) mEdit.addSeparator();
            mEdit.add(mEditMenuItem[x]);
            mEditMenuItem[x].addActionListener(this);
        }

        for (x = 0; x < 1; x++) {
            mFormatMenuItem[x] = new JMenuItem(strFormatMenuItem[x]);
            mFormat.add(mFormatMenuItem[x]);
            mFormatMenuItem[x].addActionListener(this);
        }

        for (x = 0; x < 2; x++) {
            mRunMenuItem[x] = new JMenuItem(strRunMenuItem[x]);
            mRun.add(mRunMenuItem[x]);
            mRunMenuItem[x].addActionListener(this);
        }

        // ✨ New submenus for font style and alignment
        JMenu mFontStyle = new JMenu("Font Style");
        JMenuItem boldItem = new JMenuItem("Bold");
        JMenuItem italicItem = new JMenuItem("Italic");
        JMenuItem plainItem = new JMenuItem("Plain");
        mFontStyle.add(boldItem);
        mFontStyle.add(italicItem);
        mFontStyle.add(plainItem);
        mFormat.add(mFontStyle);

        JMenu mAlign = new JMenu("Alignment");
        JMenuItem leftItem = new JMenuItem("Left");
        JMenuItem centerItem = new JMenuItem("Center");
        JMenuItem rightItem = new JMenuItem("Right");
        mAlign.add(leftItem);
        mAlign.add(centerItem);
        mAlign.add(rightItem);
        mFormat.add(mAlign);

        boldItem.addActionListener(this);
        italicItem.addActionListener(this);
        plainItem.addActionListener(this);
        leftItem.addActionListener(this);
        centerItem.addActionListener(this);
        rightItem.addActionListener(this);

        // editor + output
        ta = new JTextPane();
        ta1 = new JTextArea(50, 50);
        ta.setFont(new Font("courier new", Font.PLAIN, 20));
        ta1.setFont(new Font("COURIER NEW", Font.PLAIN, 20));

        sp = new JScrollPane(ta);
        sp1 = new JScrollPane(ta1);
        sp.setBounds(0, 0, 1350, 520);
        sp1.setBounds(0, 530, 1350, 135);
        jf.add(sp);
        jf.add(sp1);

        ta.addKeyListener(this);
        r = Runtime.getRuntime();
        textchange = false;

        jf.setTitle("Editor2025 : Untitled");
        jf.setLocation(0, 0);
        jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jf.setSize(1500, 740);
        jf.setVisible(true);
    }

    public void keyPressed(KeyEvent te) {
        if (te.getSource() == ta) {
            textchange = true;
        }
    }
    public void keyReleased(KeyEvent te) {}
    public void keyTyped(KeyEvent te) {}

    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getActionCommand().equals("New")) {
                ta.setText(null);
                NewButtonCoding();
            } else if (ae.getActionCommand().equals("Open")) {
                fd = new FileDialog(this, "Open a File", FileDialog.LOAD);
                fd.setVisible(true);
                String st3 = fd.getFile();
                if (st3 == null) return;
                fis = new FileInputStream(fd.getDirectory() + fd.getFile());
                b = new byte[fis.available()];
                fis.read(b);
                ta.setText(new String(b));
                jf.setTitle("Editor2025 : " + fd.getFile());
                filename = fd.getDirectory() + fd.getFile();
                fname = fd.getFile();
                textchange = false;
            } else if (ae.getActionCommand().equals("Save")) {
                SaveButtonCoding();
            } else if (ae.getActionCommand().equals("Save As...")) {
                SaveAs();
            } else if (ae.getActionCommand().equals("Exit")) {
                System.exit(0);
            } else if (ae.getActionCommand().equals("Cut")) ta.cut();
            else if (ae.getActionCommand().equals("Paste")) ta.paste();
            else if (ae.getActionCommand().equals("Copy")) ta.copy();
            else if (ae.getActionCommand().equals("Select All")) ta.selectAll();
            else if (ae.getActionCommand().equals("Delete")) ta.replaceSelection("");
            else if (ae.getActionCommand().equals("Color")) {
                cc = new JColorChooser();
                Color newColor = cc.showDialog(this, "Select a Color", Color.BLACK);
                ta.setForeground(newColor);
                ta1.setForeground(newColor);
            }
            // ✨ Font Styles
            else if (ae.getActionCommand().equals("Bold")) {
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setBold(attr, true);
                ta.setCharacterAttributes(attr, false);
            }
            else if (ae.getActionCommand().equals("Italic")) {
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setItalic(attr, true);
                ta.setCharacterAttributes(attr, false);
            }
            else if (ae.getActionCommand().equals("Plain")) {
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setBold(attr, false);
                StyleConstants.setItalic(attr, false);
                ta.setCharacterAttributes(attr, false);
            }
            // ✨ Alignment
            else if (ae.getActionCommand().equals("Left")) {
                StyledDocument doc = ta.getStyledDocument();
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
                doc.setParagraphAttributes(0, doc.getLength(), attr, false);
            }
            else if (ae.getActionCommand().equals("Center")) {
                StyledDocument doc = ta.getStyledDocument();
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
                doc.setParagraphAttributes(0, doc.getLength(), attr, false);
            }
            else if (ae.getActionCommand().equals("Right")) {
                StyledDocument doc = ta.getStyledDocument();
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
                doc.setParagraphAttributes(0, doc.getLength(), attr, false);
            }
            else if (ae.getActionCommand().equals("Compile")) {
                SaveButtonCoding();
                if (!filename.equals("")) {
                    try {
                        result = "";
                        Process error = r.exec("javac " + filename);
                        BufferedReader err = new BufferedReader(new InputStreamReader(error.getErrorStream()));
                        String temp;
                        while ((temp = err.readLine()) != null) {
                            result += temp + "\n";
                        }
                        if (result.equals("")) {
                            ta1.setText("Compilation Successful " + filename);
                        } else {
                            ta1.setText(result);
                        }
                        err.close();
                    } catch (Exception e1) {
                        ta1.setText("" + e1);
                    }
                }
            }
            else if (ae.getActionCommand().equals("Run..")) {
                SaveButtonCoding();
                if (!fname.equals("")) {
                    try {
                        int num = fname.indexOf('.');
                        String fn = fname.substring(0, num);
                        Process p = r.exec("java " + fn);
                        BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        String temp;
                        result = "";
                        while ((temp = output.readLine()) != null) result += temp + "\n";
                        while ((temp = error.readLine()) != null) result += temp + "\n";
                        output.close();
                        error.close();
                        ta1.setText(result);
                    } catch (Exception e2) {
                        ta1.setText("" + e2);
                    }
                }
            }
            else if (ae.getActionCommand().equals("About Editor")) {
                String h1 = "This Editor has been created by Gunjan Soni \nProject under Prof. Manish Bhatia (HOD Java Deptt.).";
                JOptionPane.showMessageDialog(this, h1);
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(this, "" + e1);
        }
    }

    public void SaveButtonCoding() {
        try {
            if (filename.equals("")) {
                fd = new FileDialog(this, "Save a File", FileDialog.SAVE);
                fd.setVisible(true);
                String st = fd.getFile();
                if (st == null) return;
                fos = new FileOutputStream(fd.getDirectory() + fd.getFile());
                filename = fd.getDirectory() + fd.getFile();
                fname = fd.getFile();
            } else {
                fos = new FileOutputStream(filename);
            }
            b = ta.getText().getBytes();
            fos.write(b);
            fos.close();
            jf.setTitle("Editor2025 : " + fname);
            ta1.setText("File has been saved now.");
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "No file to save.");
        }
    }

    public void SaveAs() {
        try {
            fd = new FileDialog(this, "Save As A File", FileDialog.SAVE);
            fd.setVisible(true);
            String st1 = fd.getFile();
            if (st1 == null) return;
            fos = new FileOutputStream(fd.getDirectory() + fd.getFile());
            b = ta.getText().getBytes();
            fos.write(b);
            fos.close();
            filename = fd.getDirectory() + fd.getFile();
            fname = fd.getFile();
            jf.setTitle("Editor2025 : " + fname);
            ta1.setText("File has been Saved now.");
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "NO files to save.");
        }
    }

    public void NewButtonCoding() {
        String str = JOptionPane.showInputDialog(null, "Please Enter the class name", "Provide a class name",
                JOptionPane.PLAIN_MESSAGE);
        if (str == null) return;
        ta.setText("import java.lang.*;\n\npublic class " + str + "\n{\n\tpublic static void main(String[] s)\n\t{\n\t\t\n\t}\n}");
        jf.setTitle("Editor2025 : Untitled");
    }

    public static void main(String ag[]) {
        new Editor2015();
    }
}
