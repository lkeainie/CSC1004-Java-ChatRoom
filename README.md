# CSC1004-Java-ChatRoom
## File
**ClientFrame**           
**EmojiFrame**
**LoginFrame**
**RegisterFrame**
**MainFrame**
**ServerFrame**           The above is the GUI code for each interface.
**ServerSystem**          The code of the server's main process.
**ClientSystem**          The code of the client's main process, mainly about send message and GUI button response.
**LoginSystem**           The code of the login system.
**ServerThread**          The code of the server's child process.
**ClientThread**          The code of the client's child process, mainly about receive message.
**AudioRecorderThread**   The code of voice chat
**Sql**                   The code of SQL
**Main**                  Everything start
## Multi-Client Chat
I use socket network programming to implement multi-user chat. 
Before starting the chat, someone needs to start the server-side to launch the server, and then multiple users can chat within the local area network.
The server creates a ServerSocket to continuously receive connection requests sent by users. 
Whenever a user successfully connects, the server assigns a separate socket to receive their messages and send them to others.
## Login System
"Users can input their username and password on the login page. 
The password will be encrypted using md5 before being sent to the server for verification. 
If the username exists and the password is correct, the user can log in
## Java GUI
I use Java Swing for GUI programming, and I personally wrote and adjusted the code for each interface. This includes the use of JFrame, JPanel, and other components.
Clicking on the software icon allows you to choose between the client or server side. 
**The server side** can start and stop the server, while **the client side** can register and log in. 
**In the chat interface**, there is a logout button in the **upper left corner** and a list of currently online users in the **lower left corner**. I used JList to implement this feature. 
**The upper right corner** displays the chat room interface, which I implemented using a JPanel nested in a JScrollPanel and added JLabels and JButtons. 
**The lower right corner** is the input message interface, where you can select the type of message and send it.
## Registration system
Users can choose to enter the registration page from the login page, which will hide the login interface. 
On the registration page, users need to enter a **unique username** (cannot be the same as an existing user), 
**password and confirm password** (which must contain at least one uppercase letter, one lowercase letter, one number, one special character, and have a length between 6-18 characters), 
**gender** (only 'male' and 'female' are allowed, regardless of case), 
**address**, 
and **age** (between 1-99). 
If any of these requirements are not met, a popup window will appear to indicate registration failure.
Upon successful registration, the user's information will be added to the server's database.
## Emoji
The client will use a for loop to initialize some of the emojis, using a char character for-loop as the first character of the emoji, with the second character fixed. 
When the user clicks the emoji button, a JFrame containing a JList will pop up, allowing the user to select the desired emoji.
## Sending Pictures
I use JFileChooser to select an image and put it into a byte array, then convert it to base64 format for sending. 
When the user receives the image (preceded by receiving the message type), the base64 is decoded and converted back to an image which is then placed in a JLabel. 
To ensure that the image size is appropriate, the client will compress the image appropriately. Therefore, I have prepared an ImageView, which will display a higher quality image when the user clicks on it. 
Of course, there is still a possibility of distortion, in order to accommodate different screen resolutions.
## Sending Videos
The same JFileChooser is used to select a file and convert it to base64 for sending. 
The file is displayed in the chat window as a button, which allows users to click to play or stop the audio message. 
When playing, a new thread will be used.
## Voice Chat
When the "voice start" button is clicked, the client will activate the microphone and record the sound into a byte array. 
The implementation details can be found in AudioRecorderThread.java. 
After that, the processing will be consistent with handling regular audio messages.
## Message History
The server will create a table to record messages using SQLite as the SQL database. 
Each message sent by a user will be recorded in chronological order, and when a user logs out, the system will record this time. 
When the user logs in again, all historical records will be sent, with slight modifications to the SQL statement to only receive offline messages. 
To prevent users from interrupting the SQL statement while entering messages and performing database injection, precompiled SQL techniques are used.
