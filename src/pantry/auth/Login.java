package pantry.auth;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Login class represents login and registration UI
 */
public class Login implements DocumentListener, ActionListener{
    // login page controls
    JTextField      username;
    JPasswordField  password;

    // registration page controls
    JTextField      reg_username;
    JPasswordField  reg_password;
    JPasswordField  conf_password;

    JButton         loginButton;
    JButton         regButton;
    JButton         regLinkButton;
    CardLayout      cardLayout;
    JPanel          cards;

    final String LOGIN_CARD = "LoginUser";
    final String REGISTER_CARD = "RegisterNewUser";
    HashMap<String, String> passwordDB = null;
    JDialog modelDialog = null;

    public void Show(final JFrame frame) {
        modelDialog = new JDialog(frame, "PantryWare - Manager Login", Dialog.ModalityType.DOCUMENT_MODAL);

        // set dialog box size
        modelDialog.setSize(350, 240);
        modelDialog.setResizable(false);

        // center dialog relative to its parent
        modelDialog.setLocationRelativeTo(frame);
        InitComponent(modelDialog);

        Container dialogContainer = modelDialog.getContentPane();

        // load password database, before you show the dialog
        passwordDB = LoadPasswordDb();

        if (passwordDB.size() == 0) {
            ShowUserRegistrationPage();
        }
        else
            ShowLoginPage() ;

        modelDialog.setVisible(true);
    }

    /**
     * Shows login page
     */
    public void ShowLoginPage() {
        modelDialog.setTitle("PantryWare - Login");
        cardLayout.show(cards, LOGIN_CARD);
    }

    /**
     * Shows Registration page
     */
    public void ShowUserRegistrationPage(){
        modelDialog.setTitle("PantryWare - Register New User");
        cardLayout.show(cards, REGISTER_CARD);
    }

    /**
     * Initialize components
     * @param dialog
     */
    private void InitComponent(JDialog dialog){
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        JPanel loginCard= AddLoginCard();
        JPanel registerCard = AddRegisterCard();

        cards.add(loginCard,LOGIN_CARD) ;
        cards.add(registerCard, REGISTER_CARD) ;
        dialog.add(cards);
    }


    /**
     * Adds register user card
     * @return JPanel
     */
    private JPanel AddRegisterCard() {
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(null); // take the entire with and height of the screen

        int leftx = 15;

        // add controls to acquire name and password
        JLabel unLabel = new JLabel("Username");
        unLabel.setBounds(leftx, 8, 105, 28);
        registerPanel.add(unLabel);

        reg_username = new JTextField();
        reg_username.setBounds(leftx+107, 8, 193, 28);
        registerPanel.add(reg_username);

        JLabel pwdLbl = new JLabel("Password");
        pwdLbl.setBounds(leftx, 46, 105, 28);
        registerPanel.add(pwdLbl);

        reg_password = new JPasswordField();
        reg_password.getDocument().addDocumentListener(this);
        reg_password.getDocument().putProperty("owner", reg_password); //set the owner
        reg_password.setBounds(leftx+107, 46, 193, 28);
        registerPanel.add(reg_password);

        JLabel confirm_pwd_lbl = new JLabel("Confirm Password");
        confirm_pwd_lbl.setBounds(leftx, 84, 105, 28);
        registerPanel.add(confirm_pwd_lbl);

        conf_password = new JPasswordField();
        conf_password.getDocument().addDocumentListener(this);
        conf_password.getDocument().putProperty("owner", conf_password); //set the owner
        conf_password.setBounds(leftx+107, 84, 193, 28);
        registerPanel.add(conf_password);

        regButton = new JButton("Register");
        regButton.setBounds(130, 132, 90, 25);
        regButton.setForeground(Color.WHITE);
        regButton.setBackground(Color.BLACK);
        regButton.addActionListener(this);
        regButton.setEnabled(false);
        registerPanel.add(regButton);

        // https://stackoverflow.com/questions/16115922/how-do-i-compare-two-passwords-create-password-and-confirm-password-useing-the
        // https://stackoverflow.com/questions/9354376/calling-a-method-everytime-the-text-in-jpasswordfield-is-altered
        // https://stackoverflow.com/questions/23856818/set-enable-button-if-text-field-is-fill

        return registerPanel;
    }

    /**
     * Add login card
     * @return JPanel
     */
    private JPanel AddLoginCard(){
        // create panel object
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null); // take the entire with and height of the screen

        int leftx = 75;
        // add controls to acquire name and password
        JLabel unLabel = new JLabel("Username");
        unLabel.setBounds(leftx, 8, 70, 20);
        loginPanel.add(unLabel);

        username = new JTextField();
        username.setBounds(leftx, 30, 193, 28);
        loginPanel.add(username);

        JLabel pwdLbl = new JLabel("Password");
        pwdLbl.setBounds(leftx, 68, 70, 20);
        loginPanel.add(pwdLbl);

        password = new JPasswordField();
        password.setBounds(leftx, 90, 193, 28);
        loginPanel.add(password);

        loginButton = new JButton("Login");
        loginButton.setBounds(130, 128, 90, 25);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(Color.BLACK);
        loginButton.addActionListener(this);
        loginPanel.add(loginButton);

        regLinkButton = new JButton();
        regLinkButton.setText("<HTML>No Account? <FONT color=\"#000099\"><U>Click here!</U></FONT></HTML>");
        regLinkButton.setBounds(leftx, 163, 200, 25);
        regLinkButton.setHorizontalAlignment(SwingConstants.LEFT);
        regLinkButton.setBorderPainted(false);
        regLinkButton.setOpaque(false);
        regLinkButton.setBackground(Color.WHITE);
        regLinkButton.setToolTipText("Create new account!");
        regLinkButton.addActionListener(this);
        loginPanel.add(regLinkButton);

        return loginPanel;
    }

    /**
     * Enable or Disable register button
     */
    private void UpdateRegisterButton(DocumentEvent e){
        Object owner = e.getDocument().getProperty("owner");
        if (owner == reg_password || owner == conf_password) {
            boolean validPassword = (reg_password.getPassword().length >=8 && true == Arrays.equals(reg_password.getPassword(), conf_password.getPassword()));
            regButton.setEnabled(validPassword);
        }
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ( e.getSource() == loginButton) {
            if (!Authenticate(username.getText(), password.getPassword()))
                JOptionPane.showMessageDialog(null, "Username or Password mismatch ");
            else {
                // close login dialog
                modelDialog.dispose();
            }
        }
        else if (e.getSource() == regLinkButton){
            reg_username.setText("");
            reg_password.setText("");
            conf_password.setText("");
            ShowUserRegistrationPage();
        }
        else if (e.getSource() == regButton) {
            passwordDB.put(reg_username.getText(), hash(reg_password.getPassword()));
            SavePasswordDb(passwordDB);
            username.setText(reg_username.getText());
            ShowLoginPage();
            password.requestFocus();
        }
    }

    /**
     * Hash input array of charactrs
     * @param chars array of characters
     * @return hash as string
     */
    private String hash(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data

        // MessageDigest instance for hashing using SHA256
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            var digest = md.digest(bytes);

            //Converting the byte array in to HexString format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0;i<digest.length;i++) {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }

            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown : " + e);
        }
        catch (NullPointerException e) {
            System.out.println("Exception thrown : " + e);
        }
        catch (Exception e) {
            System.out.println("Exception thrown : " + e);
        }

        return "";
    }

    /**
     * Loads password database
     * @return map of username and password
     */
    private HashMap<String, String> LoadPasswordDb(){
        HashMap<String, String> map = null ;
        try
        {
            var fis = new FileInputStream("pantry.usr.db");
            ObjectInputStream ois = new ObjectInputStream(fis);

            if (ois != null) {
                map = (HashMap<String, String>)ois.readObject();
            }

            ois.close();
            fis.close();

            return map;
        }
        catch (FileNotFoundException fife){
            System.out.println("No password database exists");
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (map == null) {
            map = new HashMap<String, String>();
        }
        return map;
    }

    /**
     * Save passwords database
     * @param pwdMap has map containing passwords
     * @return true if passwords are saved, else false
     */
    private boolean SavePasswordDb(HashMap<String, String> pwdMap){
        boolean saved = false;

        try
        {
            var fos = new FileOutputStream("pantry.usr.db");
            var oos = new ObjectOutputStream(fos);

            if (oos != null) {
                oos.writeObject(pwdMap);
                saved = true;
            }

            oos.close();
            fos.close();
        }
        catch (FileNotFoundException fife){
            System.out.println("Failed to open password database");
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }

        return saved;
    }

    /**
     * Authenticate user
     * @param username username
     * @param password password specified by the user entry
     * @return true if successfully authenticated
     */
    private boolean Authenticate(String username, char[] pwd) {
        boolean authenticated = false;
        boolean regRequired = true;
        String verifyHash = hash(pwd) ;
        if (!verifyHash.isEmpty()){
            if (passwordDB != null){
                if (passwordDB.containsKey(username)){
                    regRequired = false;
                    String pwdHash = passwordDB.get(username);
                    if (pwdHash.compareToIgnoreCase(verifyHash) == 0)
                        authenticated = true;
                }
            }
        }

        return authenticated ;
    }

    /**
     * Gives notification that there was an insert into the document.  The
     * range given by the DocumentEvent bounds the freshly inserted region.
     *
     * @param e the document event
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
            UpdateRegisterButton(e);
    }

    /**
     * Gives notification that a portion of the document has been
     * removed.  The range is given in terms of what the view last
     * saw (that is, before updating sticky positions).
     *
     * @param e the document event
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
            UpdateRegisterButton(e);
    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
          UpdateRegisterButton(e);
    }
}
