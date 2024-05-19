x = linspace(0,pi,1000);
figure
plot(x,MAfilter(x,5));
legend('n=5')
figure
plot(x,MAfilter(x,20));
legend('n=20')

figure
stock = readtable("GOOG.csv");
s = stock{:,5};
fiveDay = MAstock(s,5);
twentyDay = MAstock(s,20);
hundDay = MAstock(s,100);

x=1:length(fiveDay);
plot(x,fiveDay)
hold on
x=1:length(twentyDay);
plot(x,twentyDay)

x=1:length(hundDay);
plot(x,hundDay)

plot(s)
legend('5 day','20 day','100 day','stock price')
hold off

function h = MAfilter(x,n)
    h=0;
    for i = 1:((n-1)/2)
            h = h + 2*cos(x*i);
    end
    h = h+1;
    
    if rem(n,2) == 0   
        h = 1/n*abs(1 + exp(-1i*x*n/2).*h);
    else
        h = 1/n*abs(h);
    end
end

function y = MAstock(s,n)
    y=[];
    %for index below n,size of moving average, slowly incresse the window
    %until index equals size of moving value.
    for i=1:n-1
        k=0
        for j=0:i-1
            k=k+s(i-j)
        end
        y(end+1) = k*1/i;
    end 
    %for index >= n. standard moving average calculation.
    for i=n:length(s)
        k=0
        for j=0:(n-1)
            k = k + s(i-j)
        end
        y(end+1) = 1/n*k;
    end
end