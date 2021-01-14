#!/usr/bin/env python
import SimpleHTTPServer

from BaseHTTPServer import HTTPServer


class RequestHandler(SimpleHTTPServer.SimpleHTTPRequestHandler):

    def do_GET(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        request_headers = self.headers.dict
        content = "Hello, World!"
        self.wfile.write(content)
        return

def main():
    port = 8000
    print('Listening on port:%s' % port)
    server = HTTPServer(('', port), RequestHandler)
    server.serve_forever()

if __name__ == "__main__":
    main()