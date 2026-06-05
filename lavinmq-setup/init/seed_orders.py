import pika
import uuid
import json

def generate_ticket(movie_title, date_time, total_ticket):
    return {
        "id": str(uuid.uuid4()),
        "movieTitle": movie_title,
        "dateTime": date_time,
        "totalTicket": total_ticket
    }

def publish_tickets():
    connection = pika.BlockingConnection(pika.ConnectionParameters(
        host='messagebroker.device.dev',
        port=5672,
        credentials=pika.PlainCredentials('guest', 'guest')
    ))
    channel = connection.channel()

    tickets = [
        generate_ticket("Inception", "2026-06-20T19:30:00", 2),
        generate_ticket("The Dark Knight", "2026-06-21T20:00:00", 1),
        generate_ticket("Interstellar", "2026-06-22T18:00:00", 3),
    ]

    for ticket in tickets:
        channel.basic_publish(
            exchange='',
            routing_key='send-queue',
            body=json.dumps(ticket),
            properties=pika.BasicProperties(
                delivery_mode=2,
                content_type='application/json'
            )
        )
        print(f"✔ Ticket published: {ticket['movieTitle']} on {ticket['dateTime']}")

    connection.close()

if __name__ == "__main__":
    publish_tickets()
