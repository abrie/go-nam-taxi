#!/usr/bin/env bash
LAYOUT="bb21,181x55,0,0{80x55,0,0[80x27,0,0,0,80x27,0,28,2],100x55,81,0,1}"
SESSION="GoNamTaxi"
if ! tmux has-session -t $SESSION 2>/dev/null
then
    tmux -2 new-session -d -s $SESSION \;\
        split-window \;\
        split-window \;\
        select-layout $LAYOUT \;\
        send-keys -t 0 "nodemon server.js" C-m \;\
        send-keys -t 1 "git log --oneline" C-m \;\
        send-keys -t 2 "vim" C-m C-n
fi
tmux -2 attach-session -t $SESSION
