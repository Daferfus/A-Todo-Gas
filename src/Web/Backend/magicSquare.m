function out = magicSquare(n)
if ischar(n)
  n = str2double(n);
end
out = magic(n);
csvwrite('result.csv', out);