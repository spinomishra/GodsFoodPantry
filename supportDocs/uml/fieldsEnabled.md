classDiagram
direction BT
class Activities {
<<enumeration>>
  +  Food_Serving
  +  Stacking
  +  Generic
  +  Harvesting
  +  FoodDelivery
  +  FundRaising
  +  Packaging
  +  Cleaning
}
class ActivityInfo {
  - long serialVersionUID
  - LocalDateTime end_time
  - Activities activity
  - LocalDateTime start_time
}
class Consumer {
  - long serialVersionUID
  - String signature
  - boolean group
  - Identity identityInfo
  - String signaturePNG
  - short groupMember
}
class ConsumerInfo {
  + JFormattedTextField issuedOn
  - JButton signNow
  + JTextField idNumber
  + JComboBox stateBox
  + JCheckBox groupCheckBox
  + DeskSign deskSign1
  + JComboBox idTypeBox
  + JSpinner spinner
  + JFormattedTextField expiryFld
}
class ConsumerTable {
  - ConsumerTableModel consumerModel
  - Color selectedRowColor
  - ITableSelectionChangeListener selectionChangeListener
  - JLabel signControl
  - ArrayList~Consumer~ consumers
}
class ConsumerTableModel {
  - String[] COLUMN_NAMES
}
class DateHelper {
  + Date MAX_DATE
}
class DistributionHome {
  - ArrayList~Consumer~ todaysConsumers
  - ArrayList~Consumer~ oldConsumers
  ~ JComboBox comboBox
  - String pantryName
  - ConsumerTable consumerTable
  ~ JButton deleteBtn
}
class Employee {
  - long serialVersionUID
  - String SSNHash
  - EmployeeRole Role
}
class EmployeeInfo {
  - EmployeeRole Role
  ~ JFormattedTextField ssnField
  - JComboBox~EmployeeRole~ roleList
  ~ JFormattedTextField empStart
}
class EmployeeManagerCard {
  + String Title
  - ArrayList~Employee~ employees
  - String removeLabel
  - Window parentWindow
  - String addNew
  - String printLabel
  - EmployeeTableModel employeeModel
  - JButton removeButton
  - ListSelectionModel employeeTableListModel
  - JButton printButton
  - JTable employeeTable
  - JButton newButton
}
class EmployeeRole {
<<enumeration>>
  +  Cleaner
  +  Cook
  +  Validator
  +  Undecided
  +  Assistant
  +  Owner
  +  Driver
  +  Accountant
  +  Manager
}
class EmployeeRoleCellEditor {
  - EmployeeRole role
  - List~EmployeeRole~ listRole
}
class EmployeeRoleCellRenderer
class EmployeeTableModel {
  - String[] COLUMN_NAMES
}
class ExecutionModeSelection {
  + boolean rememberMe
  + String executionMode
}
class FileAdapter {
  - String fileName
  ~ ObjectInputStream ois
  - FileInputStream fis
  ~ IPantryData pantryData
}
class Hash
class Home {
  - String pantryName
}
class IDType {
<<enumeration>>
  +  DriverLicense
  +  PassPort
  +  StateId
}
class IDataConnector {
<<Interface>>

}
class IHome {
<<Interface>>

}
class IPantryData {
<<Interface>>

}
class ITableSelectionChangeListener {
<<Interface>>

}
class Identity {
  + String Number
  + int IdentityType
  + String IssuedByState
  + String ExpiresOn
  - long serialVersionUID
  + String IssuedOn
}
class Login {
  - JPasswordField password
  - JPasswordField reg_password
  - JButton regButton
  - JTextField reg_username
  - boolean authenticated
  - JPasswordField conf_password
  - String REGISTER_CARD
  - JPanel cards
  - JButton regLinkButton
  - JTextField username
  - String LOGIN_CARD
  - String activePage
  - HashMap~String, String~ passwordDB
  - JDialog modelDialog
  - CardLayout cardLayout
  - JButton loginButton
}
class MainPanel {
  - Map~String, JPanel~ cards
  - CardLayout cardLayout
  - JFrame parentWindow
}
class ManagementHome {
  ~ JPanel jPanel1
  ~ SideMenuPanel sideMenuPanel
  ~ MainPanel mainPanel
  ~ String pantryName
  ~ Map~String, JPanel~ mainPanelCards
}
class Pantry {
  ~ PantryData pantryRecords
  ~ Pantry instance
}
class PantryData {
  - ArrayList~Employee~ employees
  - String recordsFileName
  - ArrayList~Volunteer~ volunteers
}
class Person {
  - String Address
  - long serialVersionUID
  - String Mobile_number
  - String fName
  - String lName
  - int Id
}
class PersonInfo {
  # int option
  # JFormattedTextField contactTextBox
  # JButton cancelButton
  # JPanel dialogPane
  # JButton okButton
  # JTextArea addressTextBox
  # String personName
  # String personContact
  # JTextField nameTextBox
  # String personAddress
  # JTabbedPane tabbedPane
}
class PhoneHelper
class PrintHelper
class RowTableModel~T~ {
  # Class[] columnClasses
  # Boolean[] isColumnEditable
  # List~T~ modelData
  # List~String~ columnNames
  - boolean isModelEditable
  - Class rowClass
}
class SideMenuGridLayout
class SideMenuItem {
  + int MENUITEM_WIDTH
  - ImageIcon icon_hover
  - ImageIcon icon_pressed
  - Color foregroundColor
  - Color hoverColor
  + int MENUITEM_HEIGHT
  - Color backgroundColor
  - ImageIcon icon
}
class SideMenuPanel {
  - GroupLayout gl
  - int currentWidth
  - int speed
  - boolean isOpen
  - JFrame frame
  - JPanel sideBar
  - JPanel mainContentPanel
  - int minWidth
  - int responsiveMinWidth
  - JPanel menuBar
  - int maxWidth
  - boolean isEnabled
}
class State {
<<enumeration>>
  +  TX
  +  WI
  +  AR
  +  GA
  +  OH
  +  CT
  +  ID
  +  CO
  +  NV
  +  OR
  +  VT
  +  OK
  +  NY
  +  DE
  +  HI
  +  KS
  +  NJ
  +  AL
  +  SC
  +  WV
  +  WA
  +  NM
  +  LA
  +  CA
  +  PA
  - String state
  +  MI
  +  AZ
  +  MN
  +  NH
  +  RI
  +  ND
  +  FL
  +  IA
  +  SD
  +  WY
  +  VA
  +  TN
  +  IL
  +  UT
  +  MA
  +  KY
  +  ME
  +  IN
  +  MD
  +  MT
  +  MO
  +  NC
  +  AK
  +  NE
  +  MS
}
class StringHelper {
  + String Empty
}
class Tile {
  - int DefaultColor
  - int MouseHoverColor
  - int MousePressedColor
}
class TileManager {
  + String Title
  - Window parentWindow
}
class Volunteer {
  - ArrayList~ActivityInfo~ activityHistory
  - long serialVersionUID
}
class VolunteerCheckout {
  - JDialog modelDialog
  - JComboBox~Volunteer~ recentVolunteersComboBox
  - Volunteer checkoutVolunteer
}
class VolunteerHome {
  - Tile checkinTile
  ~ String pantryName
  - Tile checkoutTile
  - ArrayList~Volunteer~ recentVolunteers
}
class VolunteerInfo {
  - JComboBox~Activities~ activitiesComboBox
  - ActivityInfo recentActivity
  ~ JTextField checkInTime
}
class VolunteerManagerCard {
  - VolunteerTable volunteerTable
  ~ JComboBox comboBox
  ~ JTextField searchNameField
  + String Title
  - JButton removeButton
  - String removeLabel
}
class VolunteerRegistration
class VolunteerTable {
  - ITableSelectionChangeListener selectionChangeListener
  - VolunteerTableModel volunteerModel
  - TableRowSorter~VolunteerTableModel~ sorter
  - Color selectedRowColor
  - ListSelectionModel tableListSelectionModel
}
class VolunteerTableModel {
  - String[] COLUMN_NAMES
}

ActivityInfo  -->  Activities 
Consumer  -->  Person 
ConsumerInfo  -->  PersonInfo 
ConsumerTableModel  -->  RowTableModel~T~ 
DistributionHome  ..>  IHome 
DistributionHome  ..>  IPantryData 
DistributionHome  ..>  ITableSelectionChangeListener 
Employee  -->  Person 
EmployeeInfo  -->  PersonInfo 
Employee  -->  EmployeeRole 
EmployeeTableModel  -->  RowTableModel~T~ 
FileAdapter  ..>  IDataConnector 
Identity  -->  IDType 
ManagementHome  ..>  IHome 
PantryData  ..>  IPantryData 
Volunteer  -->  Person 
VolunteerHome  ..>  IHome 
VolunteerInfo  -->  PersonInfo 
VolunteerManagerCard  ..>  ITableSelectionChangeListener 
VolunteerTableModel  -->  RowTableModel~T~ 
