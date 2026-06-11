import pika
import uuid
import json

def generate_ticket(movie_title, date_time, total_ticket, poster_url, actors, description, duration, genre, release_date, director):
    return {
        "id": str(uuid.uuid4()),
        "movieTitle": movie_title,
        "dateTime": date_time,
        "totalTicket": total_ticket,
        "posterUrl": poster_url,
        "actors": actors,
        "description": description,
        "duration": duration,
        "genre": genre,
        "releaseDate": release_date,
        "director": director
    }

def publish_tickets():
    connection = pika.BlockingConnection(pika.ConnectionParameters(
        host='messagebroker.device.dev',
        port=5672,
        credentials=pika.PlainCredentials('guest', 'guest')
    ))
    channel = connection.channel()

    tickets = [
        generate_ticket(
            "Inception", "2026-06-20T19:30:00", 2,
            "https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
            ["Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"],
            "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
            148, ["Action", "Science Fiction", "Adventure"],
            "2010-07-16", "Christopher Nolan"
        ),
        generate_ticket(
            "The Dark Knight", "2026-06-21T20:00:00", 1,
            "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
            ["Christian Bale", "Heath Ledger", "Aaron Eckhart"],
            "Batman raises the stakes in his war on crime. With the help of Lt. Jim Gordon and District Attorney Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets.",
            152, ["Drama", "Action", "Crime", "Thriller"],
            "2008-07-18", "Christopher Nolan"
        ),
        generate_ticket(
            "Interstellar", "2026-06-22T18:00:00", 3,
            "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
            ["Matthew McConaughey", "Anne Hathaway", "Jessica Chastain"],
            "The adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.",
            169, ["Adventure", "Drama", "Science Fiction"],
            "2014-11-05", "Christopher Nolan"
        ),
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
