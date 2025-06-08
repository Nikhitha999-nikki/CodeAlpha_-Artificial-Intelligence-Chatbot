import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatBotGui extends JFrame {

    private JTextPane chatPane;
    private JTextField chatField;
    private JButton sendButton;
    private simpleNLP bot;
    private StyledDocument doc;
    private Style userStyle, botStyle;
    private SimpleDateFormat timeFormat;

    public ChatBotGui() {
        bot = new simpleNLP();
        timeFormat = new SimpleDateFormat("HH:mm:ss"); 

        setTitle("Java AI Chatbot");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setFont(new Font("Arial", Font.PLAIN, 16));
        doc = chatPane.getStyledDocument();

        userStyle = chatPane.addStyle("UserStyle", null);
        StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(userStyle, Color.BLUE);

        botStyle = chatPane.addStyle("BotStyle", null);
        StyleConstants.setAlignment(botStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(botStyle, Color.DARK_GRAY);

        JScrollPane scrollPane = new JScrollPane(chatPane);

        JPanel inputPanel = new JPanel(new BorderLayout());
        chatField = new JTextField();
        chatField.setFont(new Font("Arial", Font.PLAIN, 16));

        sendButton = new JButton("Send");

        chatField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputPanel.add(chatField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    private void sendMessage() {
        final String input = chatField.getText().trim();
        if (!input.isEmpty()) {
            String timeStamp = getCurrentTimeStamp();
            appendMessage("You (" + timeStamp + "): " + input + "\n", userStyle);
            chatField.setText("");
            autoScroll();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                appendMessage("Bot is typing...\n", botStyle);
                            }
                        });

                        Thread.sleep(1000); 

                        final String response = bot.getResponse(input);
                        final String responseTime = getCurrentTimeStamp();

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                removeLastLine();
                                appendMessage("Bot (" + responseTime + "): " + response + "\n", botStyle);
                                autoScroll();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private String getCurrentTimeStamp() {
        return timeFormat.format(new Date());
    }

    private void appendMessage(String message, Style style) {
        try {
            int length = doc.getLength();
            doc.insertString(length, message, style);
            doc.setParagraphAttributes(length, message.length(), style, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void removeLastLine() {
        try {
            int length = doc.getLength();
            int start = Utilities.getRowStart(chatPane, length - 1);
            doc.remove(start, length - start);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autoScroll() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JScrollBar vertical = ((JScrollPane) chatPane.getParent().getParent()).getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatBotGui().setVisible(true);
            }
        });
    }
}
