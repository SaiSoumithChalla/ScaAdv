from flask import Flask
app = Flask(__name__)
@app.route('/')
def index():
        return "Testing Flask"
if __name__ == '__main__':
        app.run(debug=True,port=5001)   //Changing Debug mode without Restarting Server  and changing port number as well




