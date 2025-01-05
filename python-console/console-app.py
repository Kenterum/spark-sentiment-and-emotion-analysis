import textwrap  # Added for wrapping text

import requests

BASE_URL = "http://someip/api"

def get_top_emotion(emotion, limit):
    # GET /api/top/{emotion}?limit={limit}
    url = f"{BASE_URL}/top/{emotion}?limit={limit}"
    response = requests.get(url)
    response.raise_for_status()
    return response.json()  # List of results

def get_review_text(asin, user_id, timestamp):
    # GET /api/review/{asin}/{user_id}/{timestamp}
    url = f"{BASE_URL}/review/{asin}/{user_id}/{timestamp}"
    response = requests.get(url)
    response.raise_for_status()
    data = response.json()
    if 'error' in data:
        return None, data['error']
    return data.get('text', None), None

def main_menu():
    print("=== Emotion Analysis Console Application ===")
    print("1) View Top Comments by Emotion")
    print("Q) Quit")
    choice = input("Select an option: ").strip().lower()
    return choice

def emotion_selection():
    print("\nEnter the emotion you want to query (e.g., anger, positive, joy, etc.):")
    emotion = input("Emotion: ").strip().lower()
    return emotion

def limit_selection():
    print("\nEnter how many top comments you want to see:")
    limit_str = input("Limit: ").strip()
    if not limit_str.isdigit():
        print("Invalid input. Defaulting to 10.")
        return 10
    return int(limit_str)

def show_top_results(results, emotion):
    print(f"\nTop {len(results)} results for emotion '{emotion}':")
    for i, res in enumerate(results, start=1):
        score = res.get(emotion, 0)
        print(f"{i}) ASIN: {res['asin']}, User: {res['user_id']}, Timestamp: {res['timestamp']}, {emotion.capitalize()}: {score}")
    print("B) Back to Main Menu")
    print("Select a number to view full text, or B to go back.")
    choice = input("Choice: ").strip().lower()
    return choice

def show_review_text(asin, user_id, timestamp):
    text, error = get_review_text(asin, user_id, timestamp)
    if error:
        print(f"Error: {error}")
    else:
        print("\n=== Review Text ===")
        if text:
            # Wrap the text at 80 characters
            wrapped_text = textwrap.fill(text, width=80)
            print(wrapped_text)
        else:
            print("[No text available]")
    print("\nPress Enter to continue...")
    input()

def run_app():
    while True:
        choice = main_menu()
        if choice == 'q':
            print("Goodbye!")
            break
        elif choice == '1':
            # Emotion selection
            emotion = emotion_selection()
            # Limit selection
            limit = limit_selection()

            # Fetch top results
            try:
                results = get_top_emotion(emotion, limit)
            except requests.RequestException as e:
                print(f"Error fetching data: {e}")
                continue

            while True:
                selection = show_top_results(results, emotion)
                if selection == 'b':
                    break
                if selection.isdigit():
                    index = int(selection) - 1
                    if index < 0 or index >= len(results):
                        print("Invalid selection.")
                    else:
                        chosen = results[index]
                        asin = chosen['asin']
                        user_id = chosen['user_id']
                        timestamp = chosen['timestamp']
                        show_review_text(asin, user_id, timestamp)
                else:
                    print("Invalid choice, going back to main menu.")
                    break
        else:
            print("Invalid option. Please try again.")

if __name__ == "__main__":
    run_app()