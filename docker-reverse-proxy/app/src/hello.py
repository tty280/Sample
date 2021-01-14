from flask import Flask, request, make_response
app = Flask(__name__)

@app.route('/')
def index():
    content = "Hello, World!"
    response = make_response(content, 200)
    response.headers["Content-Type"] = "text/plain"
    return response