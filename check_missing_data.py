from pathlib import Path
import csv
import sys


def is_missing(value):
    return value is None or (isinstance(value, str) and value.strip() == "")


def check_missing_data(csv_path: Path):
    with csv_path.open("r", encoding="utf-8-sig", newline="") as f:
        reader = csv.DictReader(f)
        rows = list(reader)

    if not rows:
        print("No data found in the CSV file.")
        return

    fieldnames = reader.fieldnames or []

    missing_counts = {field: 0 for field in fieldnames}
    rows_with_missing = []

    for row_number, row in enumerate(rows, start=2):
        missing_fields = []
        for field in fieldnames:
            if is_missing(row.get(field)):
                missing_counts[field] += 1
                missing_fields.append(field)
        if missing_fields:
            rows_with_missing.append((row_number, missing_fields))

    print(f"Checked {len(rows)} rows in {csv_path}")
    print("\nMissing values per column:")
    for field in fieldnames:
        print(f"- {field}: {missing_counts[field]}")

    if rows_with_missing:
        print("\nRows with missing values:")
        for row_number, missing_fields in rows_with_missing:
            print(f"- Row {row_number}: {', '.join(missing_fields)}")
    else:
        print("\nNo missing values found.")


if __name__ == "__main__":
    if len(sys.argv) > 1:
        csv_path = Path(sys.argv[1]).resolve()
    else:
        csv_path = Path(__file__).resolve().parent / "Dataset" / "car_prices.csv"

    if not csv_path.exists():
        raise FileNotFoundError(f"CSV file not found: {csv_path}")

    check_missing_data(csv_path)
