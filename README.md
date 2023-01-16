"# pantry_manager" 
# Pantryware
Food pantry management software for local food pantry “Gods Pantry” in Plano, TX serving 400+ people weekly with fresh and packaged food.

Pantryware manages employees, volunteers, donations, and inventory at the food pantry. Pantryware can execute in administrator mode as well as volunteer mode. Administrator mode allows for management while volunteer mode allows for recording volunteer information, schedule, and participation.

This software written in Java has 3 modes -
 * Manager mode for managers to manage all records,
 * Volunteer mode for volunteers and law enforcement directed community service people to check in and checkout
 * Food distribution mode to record client information.

Managers have to register and authenticate with the application before they are provided access to the records which could be
employee, volunteering, and/or food distribution records.

Passwords are not stored in clear text and instead a SHA2 hash of the password is stored for validation. Authenticated managers have the capability to modify the recorded data. Only certain aspects of the data can be modified.

When application executes in volunteer mode, it allows only two options - Check and Checkout. All the records as serialized to files which then gets consolidated into a database. When volunteer clicks Checkin, volunteer gets presented with screen to  input their information and current time is recorded as their check-in time. Similarly for checkout, they gets presented with a screen listed all users that have an ongoing activity for today. Managers can change volunteers checkout time from management mode, if required.

When application executes in Food distribution, it allows client to input their information manually or by scanning driver license. Scanning requires a barcode reader. However both mechanisms requires client to electronically sign during information input. This is achieved using the support of inkpad signature hardware.

The managing person at food distribution counter sees all inputs as well as records for the day. Additionally last 30 days records are also visible.

Application provides capabilities to consolidate records, search a specific record, purge records, and print reports.
