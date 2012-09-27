#!/usr/bin/ruby -w

# HOW TO PROFILE:
# 1. Run unit tests
# 2. In LogCat console (Window->ShowView->LogCat), filter by Tag="ProfilerInfo" (Click the Green + icon next to "Saved Filters")
# 3. Cmd+A to select all rows, Save to "log.txt" in the same folder as parse.rb
# 4. Run the command "ruby parse.rb" to calculate an average

counter = 0
total = 0
file = File.new("log.txt", "r")
while (line = file.gets)
	line = line.split(":");
	function = line[line.length - 2].strip
	ms = line[line.length - 1].strip
	total += ms.to_i
	puts "#{function} executed in #{ms}ms"
    counter = counter + 1
end

ave = total / counter
puts "Average Execution Time: #{ave}ms."

file.close