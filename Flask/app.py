from flask import Flask ,render_template,url_for,redirect
app = Flask(__name__)
@app.route('/')
def index():
        return render_template('index.html')
@app.route('/about')
def about():
       return render_template('about.html')
@app.route('/urlfor')
def urlfor():
      return redirect(url_for('about'))   # Redirecting to one endpoint to another end point
if __name__ == '__main__':
        app.run(debug=True,port=5001)   #Changing Debug mode without Restarting Server  and changing port number as well








