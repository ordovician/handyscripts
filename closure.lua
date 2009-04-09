-- Demonstrates the usage of closures in Lua

-- Creating your own loop control structure
function loop(start, stop, block)
  local i = start
  while i < stop do
    block(i)
    i = i + 1
  end
end


local total = 2
loop(1, 3, function(i)
  total = total + i
end)
print(total)

-- Creating your own for each loop control structure
function myforeach(array, block)
  loop(1, #array + 1, function(i)
    block(array[i])
  end)
end

local array = {"a", "b", "c"}
myforeach(array, function(element)
  print(element)
end)