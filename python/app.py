from pydrive.auth import GoogleAuth
from pydrive.drive import GoogleDrive
import datetime


def upload(folder_id, file_path):
    gauth = GoogleAuth()
    gauth.LocalWebserverAuth()  # Follow the authentication steps in your web browser
    drive = GoogleDrive(gauth)
    file_name = file_path.split("/")[-1]
    current_date = datetime.datetime.now().strftime('%Y%m%d')
    new_file_name = f'backup_{current_date}'
    file = drive.CreateFile({'title': new_file_name, 'parents': [{'id': folder_id}]})
    file.SetContentFile(file_path)  # Set the content file using the file_path
    file.Upload()
    return new_file_name


# file = open("C:\\Windows\\Tempp.sql\\details")
upload("C:\\Windows\\Temp\\tempp.sql", "1-LIIbRUKZjkiuRGsREAk9rChpi-5n3oQ")

