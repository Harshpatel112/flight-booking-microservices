#!/usr/bin/env python3
from http.server import HTTPServer, BaseHTTPRequestHandler
import json
import urllib.parse
from datetime import datetime
import time

class MockAPIHandler(BaseHTTPRequestHandler):
    def _send_response(self, status_code, data):
        self.send_response(status_code)
        self.send_header('Content-Type', 'application/json')
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type, Authorization')
        self.end_headers()
        self.wfile.write(json.dumps(data).encode())

    def _get_request_body(self):
        content_length = int(self.headers.get('Content-Length', 0))
        if content_length > 0:
            body = self.rfile.read(content_length)
            return json.loads(body.decode())
        return {}

    def _check_auth(self):
        auth_header = self.headers.get('Authorization', '')
        return auth_header.startswith('Bearer ')

    def do_OPTIONS(self):
        self._send_response(200, {})

    def do_GET(self):
        parsed_path = urllib.parse.urlparse(self.path)
        path = parsed_path.path
        query = urllib.parse.parse_qs(parsed_path.query)

        # Health check
        if path == '/actuator/health':
            self._send_response(200, {
                'status': 'UP',
                'components': {
                    'db': {'status': 'UP'},
                    'redis': {'status': 'UP'}
                }
            })
            return

        # User profile
        elif path == '/api/v1/user/profile':
            if not self._check_auth():
                self._send_response(401, {'error': 'Unauthorized'})
                return
            
            self._send_response(200, {
                'id': 12345,
                'username': 'testuser123',
                'email': 'test@example.com',
                'firstName': 'John',
                'lastName': 'Doe',
                'phoneNumber': '+919876543210'
            })
            return

        # Flight search
        elif path == '/api/v1/flights/search':
            origin = query.get('origin', ['Delhi'])[0]
            destination = query.get('destination', ['Mumbai'])[0]
            departure_date = query.get('departureDate', ['2024-12-25'])[0]
            
            self._send_response(200, {
                'flights': [
                    {
                        'flightNumber': 'AI101',
                        'airline': 'Air India',
                        'origin': origin,
                        'destination': destination,
                        'departureDate': departure_date,
                        'departureTime': '10:00',
                        'arrivalTime': '12:30',
                        'price': 5000,
                        'currency': 'INR',
                        'availableSeats': 45
                    },
                    {
                        'flightNumber': 'SG202',
                        'airline': 'SpiceJet',
                        'origin': origin,
                        'destination': destination,
                        'departureDate': departure_date,
                        'departureTime': '14:00',
                        'arrivalTime': '16:30',
                        'price': 4500,
                        'currency': 'INR',
                        'availableSeats': 32
                    }
                ]
            })
            return

        # Flight details
        elif path.startswith('/api/v1/flights/flight/') and not path.endswith('/seatmap'):
            flight_number = path.split('/')[-1]
            self._send_response(200, {
                'flightNumber': flight_number,
                'airline': 'Air India',
                'aircraft': 'Boeing 737',
                'origin': 'Delhi',
                'destination': 'Mumbai',
                'departureDate': '2024-12-25',
                'departureTime': '10:00',
                'arrivalTime': '12:30',
                'duration': '2h 30m',
                'price': 5000,
                'currency': 'INR',
                'availableSeats': 45,
                'totalSeats': 180
            })
            return

        # Seat map
        elif path.endswith('/seatmap'):
            flight_number = path.split('/')[-2]
            self._send_response(200, {
                'flightNumber': flight_number,
                'aircraft': 'Boeing 737',
                'seatMap': {
                    'economy': {
                        'rows': 25,
                        'seatsPerRow': 6,
                        'availableSeats': ['1A', '1B', '2C', '3D', '4E', '5F'],
                        'occupiedSeats': ['1C', '1D', '2A', '2B']
                    }
                }
            })
            return

        # Booking details
        elif path.startswith('/api/v1/bookings/') and len(path.split('/')) == 5:
            if not self._check_auth():
                self._send_response(401, {'error': 'Unauthorized'})
                return
                
            booking_id = path.split('/')[-1]
            self._send_response(200, {
                'bookingId': booking_id,
                'flightNumber': 'AI101',
                'status': 'CONFIRMED',
                'passengers': [
                    {
                        'title': 'Mr',
                        'firstName': 'John',
                        'lastName': 'Doe',
                        'seatNumber': '12A'
                    }
                ],
                'totalAmount': 5000,
                'currency': 'INR',
                'bookingDate': '2024-12-28T08:15:00Z'
            })
            return

        # Service Registry (Eureka)
        elif path == '/' and self.headers.get('Host', '').startswith('localhost:8761'):
            self._send_response(200, {
                'applications': {
                    'versions__delta': '1',
                    'apps__hashcode': 'UP_4_',
                    'application': [
                        {
                            'name': 'API-GATEWAY',
                            'instance': [
                                {
                                    'instanceId': 'api-gateway:8085',
                                    'hostName': 'localhost',
                                    'app': 'API-GATEWAY',
                                    'ipAddr': '127.0.0.1',
                                    'status': 'UP',
                                    'port': {'$': 8085, '@enabled': 'true'}
                                }
                            ]
                        }
                    ]
                }
            })
            return

        self._send_response(404, {'error': 'Not Found'})

    def do_POST(self):
        path = self.path
        body = self._get_request_body()

        # User registration
        if path == '/api/v1/user/register':
            username = body.get('username', 'testuser123')
            email = body.get('email', 'test@example.com')
            
            self._send_response(201, {
                'id': 12345,
                'username': username,
                'email': email,
                'message': 'User registered successfully'
            })
            return

        # User login
        elif path == '/api/v1/user/login':
            username_or_email = body.get('usernameOrEmail', 'testuser123')
            
            self._send_response(200, {
                'accessToken': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NSIsInVzZXJuYW1lIjoidGVzdHVzZXIxMjMiLCJpYXQiOjE2MzQ1Njc4OTB9.mock-jwt-token',
                'refreshToken': 'refresh-token-123',
                'user': {
                    'id': 12345,
                    'username': username_or_email,
                    'email': 'test@example.com'
                }
            })
            return

        # Create booking
        elif path == '/api/v1/bookings/create':
            if not self._check_auth():
                self._send_response(401, {'error': 'Unauthorized'})
                return
                
            flight_number = body.get('flightNumber', 'AI101')
            passengers = body.get('passengers', [])
            total_amount = body.get('totalAmount', 5000)
            
            self._send_response(201, {
                'bookingId': f'BK{int(time.time())}',
                'flightNumber': flight_number,
                'status': 'CONFIRMED',
                'passengers': passengers,
                'totalAmount': total_amount,
                'currency': 'INR',
                'bookingDate': datetime.now().isoformat() + 'Z'
            })
            return

        # Process payment
        elif path == '/api/v1/payments/process':
            if not self._check_auth():
                self._send_response(401, {'error': 'Unauthorized'})
                return
                
            booking_id = body.get('bookingId', 'BK123456')
            amount = body.get('amount', 5000)
            payment_method = body.get('paymentMethod', 'CARD')
            
            self._send_response(200, {
                'paymentId': f'PAY{int(time.time())}',
                'bookingId': booking_id,
                'amount': amount,
                'currency': 'INR',
                'paymentMethod': payment_method,
                'status': 'SUCCESS',
                'transactionId': f'TXN{int(time.time())}',
                'paymentDate': datetime.now().isoformat() + 'Z'
            })
            return

        self._send_response(404, {'error': 'Not Found'})

    def do_PUT(self):
        path = self.path
        body = self._get_request_body()

        # Update user profile
        if path == '/api/v1/user/profile':
            if not self._check_auth():
                self._send_response(401, {'error': 'Unauthorized'})
                return
            
            self._send_response(200, {
                'id': 12345,
                'username': body.get('username', 'testuser123'),
                'email': body.get('email', 'test@example.com'),
                'firstName': body.get('firstName', 'John'),
                'lastName': body.get('lastName', 'Doe'),
                'phoneNumber': body.get('phoneNumber', '+919876543210'),
                'message': 'Profile updated successfully'
            })
            return

        # Update booking
        elif path.startswith('/api/v1/bookings/') and len(path.split('/')) == 5:
            if not self._check_auth():
                self._send_response(401, {'error': 'Unauthorized'})
                return
                
            booking_id = path.split('/')[-1]
            self._send_response(200, {
                'bookingId': booking_id,
                'flightNumber': body.get('flightNumber', 'AI101'),
                'status': body.get('status', 'MODIFIED'),
                'passengers': body.get('passengers', []),
                'totalAmount': body.get('totalAmount', 5000),
                'currency': 'INR',
                'lastModified': datetime.now().isoformat() + 'Z',
                'message': 'Booking updated successfully'
            })
            return

        # Update flight details (admin only)
        elif path.startswith('/api/v1/flights/flight/') and not path.endswith('/seatmap'):
            if not self._check_auth():
                self._send_response(401, {'error': 'Unauthorized'})
                return
                
            flight_number = path.split('/')[-1]
            self._send_response(200, {
                'flightNumber': flight_number,
                'airline': body.get('airline', 'Air India'),
                'aircraft': body.get('aircraft', 'Boeing 737'),
                'origin': body.get('origin', 'Delhi'),
                'destination': body.get('destination', 'Mumbai'),
                'departureTime': body.get('departureTime', '10:00'),
                'arrivalTime': body.get('arrivalTime', '12:30'),
                'price': body.get('price', 5000),
                'availableSeats': body.get('availableSeats', 45),
                'message': 'Flight details updated successfully'
            })
            return

        self._send_response(404, {'error': 'Not Found'})

    def do_DELETE(self):
        path = self.path

        # Delete user account
        if path == '/api/v1/user/account':
            if not self._check_auth():
                self._send_response(401, {'error': 'Unauthorized'})
                return
            
            self._send_response(200, {
                'message': 'User account deleted successfully',
                'deletedAt': datetime.now().isoformat() + 'Z'
            })
            return

        # Cancel booking
        elif path.startswith('/api/v1/bookings/') and len(path.split('/')) == 5:
            if not self._check_auth():
                self._send_response(401, {'error': 'Unauthorized'})
                return
                
            booking_id = path.split('/')[-1]
            self._send_response(200, {
                'bookingId': booking_id,
                'status': 'CANCELLED',
                'refundAmount': 4500.00,
                'refundStatus': 'PROCESSED',
                'cancellationDate': datetime.now().isoformat() + 'Z',
                'message': 'Booking cancelled successfully'
            })
            return

        # Delete flight (admin only)
        elif path.startswith('/api/v1/flights/flight/') and not path.endswith('/seatmap'):
            if not self._check_auth():
                self._send_response(401, {'error': 'Unauthorized'})
                return
                
            flight_number = path.split('/')[-1]
            self._send_response(200, {
                'flightNumber': flight_number,
                'status': 'DELETED',
                'deletedAt': datetime.now().isoformat() + 'Z',
                'message': 'Flight deleted successfully'
            })
            return

        self._send_response(404, {'error': 'Not Found'})

    def log_message(self, format, *args):
        print(f"[{datetime.now().strftime('%Y-%m-%d %H:%M:%S')}] {format % args}")

if __name__ == '__main__':
    server_address = ('', 8085)
    httpd = HTTPServer(server_address, MockAPIHandler)
    
    print("🚀 Mock Flight Booking API Server running on http://localhost:8085")
    print("📋 Health Check: http://localhost:8085/actuator/health")
    print("📚 Available endpoints:")
    print("   POST /api/v1/user/register")
    print("   POST /api/v1/user/login")
    print("   GET  /api/v1/user/profile")
    print("   PUT  /api/v1/user/profile")
    print("   DELETE /api/v1/user/account")
    print("   GET  /api/v1/flights/search")
    print("   GET  /api/v1/flights/flight/:flightNumber")
    print("   PUT  /api/v1/flights/flight/:flightNumber")
    print("   DELETE /api/v1/flights/flight/:flightNumber")
    print("   GET  /api/v1/flights/flight/:flightNumber/seatmap")
    print("   POST /api/v1/bookings/create")
    print("   GET  /api/v1/bookings/:bookingId")
    print("   PUT  /api/v1/bookings/:bookingId")
    print("   DELETE /api/v1/bookings/:bookingId")
    print("   POST /api/v1/payments/process")
    print("\n🎯 Ready for Postman testing!")
    
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print("\n🛑 Server stopped")
        httpd.server_close()