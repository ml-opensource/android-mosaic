#!/bin/bash

# Accept the version as a command-line argument
version=$1

# Find the line number where the version is mentioned
start=$(grep -n "\[$version\]" CHANGELOG.md | cut -d : -f 1)

# If the version is not found, use "Unreleased"
if [ -z "$start" ]; then
    version="Unreleased"
    start=$(grep -n "\[$version\]" CHANGELOG.md | cut -d : -f 1)
fi

# Increment the start line to skip the version line
start=$((start + 1))

# Find the line number of the next version
end=$(awk -v start=$start 'NR > start && /\[.*\]/ {print NR; exit}' CHANGELOG.md)

# If the next version is not found, print until the end of the file
if [ -z "$end" ]; then
    awk -v start=$start 'NR >= start' CHANGELOG.md
else
    awk -v start=$start -v end=$end 'NR >= start && NR < end' CHANGELOG.md
fi