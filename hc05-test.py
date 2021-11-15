import serial
uart_channel = serial.Serial("/dev/ttyAMA0" , baudrate=9600, timeout=2)
item=''
while 1:
  item = str(input("Scan item: "))
  print(str(item))
  if item == '818762000042' : uart_channel.write(b'818762000042\n')
  elif item == '2061118267002000' : uart_channel.write(b'2061118267002000\n')
  elif item == '840928100075' : uart_channel.write(b'840928100075\n')
  elif item == '3760295880974': uart_channel.write(b'3760295880974\n')
  else: uart_channel.write(b'unknown  barcode\n')
uart_channel.flush()
