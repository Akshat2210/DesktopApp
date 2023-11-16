import datetime
from pydrive.auth import GoogleAuth
from pydrive.drive import GoogleDrive

def upload_file_to_drive(file_path, folder_id):
    try:
        # Initialize GoogleAuth and authenticate with user's credentials
        gauth = GoogleAuth()
        gauth.LocalWebserverAuth()  # Follow the authentication steps in your web browser

        # Initialize GoogleDrive with the authenticated credentials
        drive = GoogleDrive(gauth)

        # Get the file name from the provided file path
        file_name = file_path.split("/")[-1]
        current_date = datetime.datetime.now().strftime('%Y%m%d')
        new_file_name = f'backup_{current_date}'
        # Create a new file on Google Drive within the specified folder
        file_metadata = {'title': new_file_name, 'parents': [{'id': folder_id}]}
        file = drive.CreateFile(file_metadata)

        # Set the content of the file to the local file
        file.SetContentFile(file_path)

        # Upload the file to Google Drive
        file.Upload()

        print(f"Uploaded file '{file_name}' to Google Drive folder with ID '{folder_id}'")
    except Exception as e:
        print(f"An error occurred: {str(e)}")

# Usage: Specify the file path and folder ID where you want to upload the file
file_path = "C:\\Windows\\Temp\\tempp.sql"  # Replace with the path to your file
folder_id = "1-LIIbRUKZjkiuRGsREAk9rChpi-5n3oQ"  # Replace with the ID of the destination folder on Google Drive
upload_file_to_drive(file_path, folder_id)
