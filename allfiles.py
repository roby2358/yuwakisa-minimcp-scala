#!/usr/bin/env python3
import os
import re

def find_files_by_regex(pattern, output_file="allfiles.txt"):
    """
    Traverse current directory and find files matching regex pattern.
    Append matching filenames and their contents to output_file.

    Args:
        pattern (str): Regular expression pattern to match filenames
        output_file (str): Output file to append results to
    """
    try:
        # Compile the regex pattern
        regex = re.compile(pattern)

        # Get current directory
        current_dir = os.getcwd()
        matching_files = []

        # Traverse the directory tree
        for root, dirs, files in os.walk(current_dir):
            for file in files:
                # Skip the output file to avoid including it in results
                if file == output_file:
                    continue

                # Check if filename matches the regex
                if regex.search(file):
                    # Get relative path from current directory
                    relative_path = os.path.relpath(os.path.join(root, file), current_dir)
                    matching_files.append(relative_path)

        # Append results to output file
        with open(output_file, 'a', encoding='utf-8') as f:
            f.write(f"\n--- Search Results for pattern: {pattern} ---\n")
            if matching_files:
                for file_path in sorted(matching_files):
                    f.write(f"\nPath: {file_path}\n")
                    f.write("Contents:\n")
                    try:
                        with open(file_path, 'r', encoding='utf-8') as content_file:
                            f.write(content_file.read())
                    except Exception as e:
                        f.write(f"[Error reading file: {str(e)}]\n")
                    f.write("\n" + "-"*50 + "\n")
                print(f"Found {len(matching_files)} matching files. Results appended to {output_file}")
            else:
                f.write("No matching files found.\n")
                print("No matching files found.")
            f.write("--- End of Search ---\n\n")

    except Exception as e:
        print(f"An error occurred: {e}")

def main():
    # Hard-coded regex pattern - modify as needed
    pattern = r"\.scala$"  # Example: matches Scala files

    print(f"Searching for files matching pattern: {pattern}")
    find_files_by_regex(pattern)

if __name__ == "__main__":
    main()